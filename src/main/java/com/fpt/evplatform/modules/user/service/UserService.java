package com.fpt.evplatform.modules.user.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.fpt.evplatform.common.enums.PostStatus;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import com.fpt.evplatform.modules.membership.repository.MembershipPlanRepository;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.mapper.SalePostMapper;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.user.dto.request.UserCreationRequest;
import com.fpt.evplatform.modules.user.dto.request.UserUpdateRequest;
import com.fpt.evplatform.modules.user.dto.response.UserResponse;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.mapper.UserMapper;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    MembershipPlanRepository membershipPlanRepository;
    SalePostRepository salePostRepository;
    Cloudinary cloudinary;
    SalePostRepository saleRepo;

    public UserResponse createUser(UserCreationRequest request, String role){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        MembershipPlan defaultPlan = membershipPlanRepository.findByName("Free")
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));
        user.setPlan(defaultPlan);
        user.setRole(role);
        user.setStatus("ACTIVE");
        userRepository.save(user);
        System.out.println(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(Integer userId){
        userRepository.deleteById(userId);
    }

    public void banUser(Integer userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus("BANNED");
        List<SalePost> posts = salePostRepository.findBySeller(user);
        posts.forEach(post -> post.setStatus(PostStatus.HIDDEN));
        userRepository.save(user);
    }


    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));


        Integer used = saleRepo.countBySeller_UserIdAndCreatedAtBetween(
                user.getUserId(), user.getStartAt(), user.getEndAt()
        );
        Integer maxPosts = user.getPlan().getMaxPosts();
        Integer remaningPosts = maxPosts - used;

        UserResponse dto = userMapper.toUserResponse(user);

        dto.setQuotaRemaining(remaningPosts);

        if (user.getAvatarPublicId() != null) {
            String thumbUrl = cloudinary.url()
                    .resourceType("image")
                    .secure(true)
                    .transformation(new Transformation()
                            .width(256).height(256)
                            .crop("thumb").gravity("face")
                            .quality("auto").fetchFormat("auto"))
                    .generate(user.getAvatarPublicId());
            dto.setAvatarThumbUrl(thumbUrl);
        }

        return dto;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponse> getUsers(){
        log.info("In method get Users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse updateUser(Integer userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(Integer id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

//    public List<PostResponse> getMyPosts(){
//        return salePostMapper.toPostResponse(salePostRepository.findBySellerId())
//    }


}

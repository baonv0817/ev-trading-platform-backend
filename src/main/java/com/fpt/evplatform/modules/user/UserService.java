package com.fpt.evplatform.modules.user;

import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.common.exception.ErrorCode;
import com.fpt.evplatform.modules.user.dto.request.UserCreationRequest;
import com.fpt.evplatform.modules.user.dto.request.UserUpdateRequest;
import com.fpt.evplatform.modules.user.dto.response.UserResponse;
import com.fpt.evplatform.modules.user.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPlanId(1);
        user.setPlanStatus("FREE");
        System.out.println("FREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREE");
        System.out.println(user.getCreatedAt());

        User saved = userRepository.save(user);
        System.out.println("FREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREEFREE");
        System.out.println(saved.getCreatedAt());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Integer userId){
        userRepository.deleteById(userId);
    }

    public List<UserResponse> getUsers(){

        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(Integer id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser(Integer userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

}

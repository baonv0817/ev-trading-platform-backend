package com.fpt.evplatform.modules.salepost.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.enums.PostStatus;
import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.batterypost.entity.BatteryPost;
import com.fpt.evplatform.modules.salepost.dto.request.CreatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.mapper.SalePostMapper;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SalePostService {
    SalePostRepository saleRepo;
    UserRepository userRepo;
    SalePostMapper postMapper;

    @Transactional
    public PostResponse createPost(String username, CreatePostRequest req) {
        User user = userRepo.findByUsername(username).orElseThrow();

        if (user.getPlan().getName().equalsIgnoreCase("FREE")) throw new AppException(ErrorCode.SALE_POST_MEMBERSHIP_REQUIRED);
        if (!"ACTIVE".equalsIgnoreCase(user.getPlanStatus())) throw new AppException(ErrorCode.SALE_POST_PLAN_EXPIRED);
        LocalDateTime now = LocalDateTime.now();
        if (user.getStartAt() == null || user.getEndAt() == null ||
                now.isBefore(user.getStartAt()) || now.isAfter(user.getEndAt())) {
            throw new AppException(ErrorCode.SALE_POST_PLAN_EXPIRED);
        }

        // 3) Check quota bài đăng trong kỳ [start_at, end_at]
        Integer used = saleRepo.countBySellerIdAndCreatedAtBetween(
                user.getUserId(), user.getStartAt(), user.getEndAt()
        );
        Integer maxPosts = user.getPlan().getMaxPosts();
        if (maxPosts != null && used >= maxPosts) {
            throw new AppException(ErrorCode.SALE_POST_LIMIT_REACHED);
        }

        // 4) Snapshot priority từ plan
        Integer priority = user.getPlan().getPriorityLevel() == null ? 0 : user.getPlan().getPriorityLevel();


        // 5) Validate detail theo productType
        if (req.getProductType() == ProductType.BATTERY) {
            if (req.getBattery() == null) throw new AppException(ErrorCode.BATTERY_DETAIL_REQUIRED);
//            if (req.getVehicle() != null) throw new IllegalArgumentException("Vehicle detail must be null for BATTERY");
        } else {
//            if (req.getVehicle() == null) throw new IllegalArgumentException("Vehicle detail required");
            if (req.getBattery() != null) throw new AppException(ErrorCode.BATTERY_DETAIL_MUST_BE_NULL);
        }

        // 6) Tạo SALE_POST
        SalePost post = SalePost.builder()
                .sellerId(user.getUserId())
                .productType(req.getProductType())
                .description(req.getDescription())
                .askPrice(req.getAskPrice())
                .provinceCode(req.getProvinceCode())
                .districtCode(req.getDistrictCode())
                .wardCode(req.getWardCode())
                .street(req.getStreet())
                .status(PostStatus.ACTIVE)
                .priorityLevel(priority)
                .build();

        // 7) Tạo DETAIL + gán vào post (OneToOne, cascade ALL)
        if (req.getProductType() == ProductType.BATTERY) {
            var d = req.getBattery();
            BatteryPost bp = BatteryPost.builder()
                    .chemistryName(d.getChemistryName())
                    .capacityKwh(d.getCapacityKwh())
                    .sohPercent(d.getSohPercent())
                    .cycleCount(d.getCycleCount())
                    .build();
            post.setBatteryPost(bp);
        }
//         else {
//            var d = req.getVehicle();
//            VehiclePost vp = VehiclePost.builder()
//                    .brand(d.getBrand())
//                    .model(d.getModel())
//                    .yearOfManufacture(d.getYear())
//                    .mileageKm(d.getMileageKm())
//                    .batteryHealthPct(d.getBatteryHealthPct())
//                    .build();
//            post.setVehiclePost(vp);
//        }
        System.out.println(post);
        // 8) Lưu tất cả trong 1 transaction (cascade sẽ tạo luôn detail)
        saleRepo.save(post);
        System.out.println(post);
        return postMapper.toPostResponse(post);

    }
}

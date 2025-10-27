package com.fpt.evplatform.modules.salepost.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.enums.PostStatus;
import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.batterypost.entity.BatteryPost;
import com.fpt.evplatform.modules.media.service.MediaService;
import com.fpt.evplatform.modules.model.entity.Model;
import com.fpt.evplatform.modules.model.repository.ModelRepository;
import com.fpt.evplatform.modules.salepost.dto.request.CreatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.request.UpdatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.mapper.SalePostMapper;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import com.fpt.evplatform.modules.vehiclepost.entity.VehiclePost;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SalePostService {
    SalePostRepository saleRepo;
    UserRepository userRepo;
    SalePostMapper postMapper;
    ModelRepository modelRepo;
    MediaService mediaService;
    SalePostQueryService salePostQueryService;

    @Transactional
    public PostResponse createPost(String username, CreatePostRequest req, @Nullable List<MultipartFile> files) {
        User user = userRepo.findByUsername(username).orElseThrow();

        if (user.getPlan().getName().equalsIgnoreCase("FREE")) throw new AppException(ErrorCode.SALE_POST_MEMBERSHIP_REQUIRED);
        if (!"ACTIVE".equalsIgnoreCase(user.getPlanStatus())) throw new AppException(ErrorCode.SALE_POST_PLAN_EXPIRED);
        LocalDateTime now = LocalDateTime.now();
        if (user.getStartAt() == null || user.getEndAt() == null ||
                now.isBefore(user.getStartAt()) || now.isAfter(user.getEndAt())) {
            throw new AppException(ErrorCode.SALE_POST_PLAN_EXPIRED);
        }

        // 3) Check quota bài đăng trong kỳ [start_at, end_at]
        Integer used = saleRepo.countBySeller_UserIdAndCreatedAtBetween(
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
          if (req.getVehicle() != null) throw new AppException(ErrorCode.VEHICLE_DETAIL_MUST_BE_NULL);
        } else {
           if (req.getVehicle() == null) throw new AppException(ErrorCode.VEHICLE_DETAIL_REQUIRED);
           if (req.getBattery() != null) throw new AppException(ErrorCode.BATTERY_DETAIL_MUST_BE_NULL);
        }

        // 6) Tạo SALE_POST
        SalePost post = SalePost.builder()
                .seller(user)
                .productType(req.getProductType())
                .title(req.getTitle())
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
        else {
           var d = req.getVehicle();
           Model model = modelRepo.findById(d.getModelId())
                   .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));
           VehiclePost vp = VehiclePost.builder()
                     .model(model)
                     .year(d.getYear())
                     .odoKm(d.getOdoKm())
                     .vin(d.getVin())
                     .transmission(d.getTransmission())
                     .fuelType(d.getFuelType())
                     .origin(d.getOrigin())
                     .bodyStyle(d.getBodyStyle())
                     .seatCount(d.getSeatCount())
                     .color(d.getColor())
                     .accessories(d.isAccessories())
                     .registration(d.isRegistration())
                     .build();
            post.setVehiclePost(vp);
        }
        // 8) Lưu tất cả
        saleRepo.save(post);
        Integer listingId = post.getListingId();
        if (files != null && !files.isEmpty()) {
            mediaService.upload(listingId, files);
        }
        return salePostQueryService.getDetail(listingId);
    }

    @Transactional
    public PostResponse update(Integer listingId, UpdatePostRequest req) {
        SalePost sp = saleRepo.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("SalePost not found"));

        // ===== Scalars =====
        if (req.getTitle() != null) {
            String t = req.getTitle().trim();
            if (t.isEmpty()) throw new IllegalArgumentException("Title cannot be empty");
            if (t.length() > 255) throw new IllegalArgumentException("Title too long (max 255)");
            sp.setTitle(t);
        }
        if (req.getDescription() != null) sp.setDescription(req.getDescription());
        if (req.getAskPrice() != null) {
            BigDecimal price = req.getAskPrice();
            if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("askPrice must be >= 0");
            sp.setAskPrice(price);
        }
        if (req.getProvinceCode() != null) sp.setProvinceCode(req.getProvinceCode());
        if (req.getDistrictCode() != null) sp.setDistrictCode(req.getDistrictCode());
        if (req.getWardCode() != null)     sp.setWardCode(req.getWardCode());
        if (req.getStreet() != null)       sp.setStreet(req.getStreet());
        if (req.getPriorityLevel() != null) sp.setPriorityLevel(req.getPriorityLevel());
        if (req.getStatus() != null)        sp.setStatus(req.getStatus());

        if (sp.getProductType() == ProductType.BATTERY) {
            applyBatteryDetail(sp, req);
        } else if (sp.getProductType() == ProductType.VEHICLE) {
            applyVehicleDetail(sp, req);
        }

        saleRepo.save(sp);
        return postMapper.toPostResponse(sp);
    }

    private void applyBatteryDetail(SalePost sp, UpdatePostRequest req) {
        var detail = req.getBattery();
        if (detail == null) return;

        BatteryPost bp = sp.getBatteryPost();
        if (bp == null) bp = new BatteryPost();

        bp.setChemistryName(detail.getChemistryName());
        bp.setCapacityKwh(detail.getCapacityKwh());
        bp.setSohPercent(detail.getSohPercent());
        bp.setCycleCount(detail.getCycleCount());

        sp.setBatteryPost(bp);
    }

    private void applyVehicleDetail(SalePost sp, UpdatePostRequest req) {
        var detail = req.getVehicle();
        if (detail == null) return;

        VehiclePost vp = sp.getVehiclePost();
        if (vp == null) vp = new VehiclePost();

        vp.setYear(detail.getYear());
        vp.setOdoKm(detail.getOdoKm());
        vp.setVin(detail.getVin());
        vp.setTransmission(detail.getTransmission());
        vp.setFuelType(detail.getFuelType());
        vp.setOrigin(detail.getOrigin());
        vp.setBodyStyle(detail.getBodyStyle());
        vp.setSeatCount(detail.getSeatCount());
        vp.setColor(detail.getColor());
        vp.setAccessories(detail.isAccessories());
        vp.setRegistration(detail.isRegistration());

        sp.setVehiclePost(vp);
    }

    @Transactional
    public void delete(Integer listingId) {
        SalePost sp = saleRepo.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("SalePost not found"));

        sp.setStatus(PostStatus.HIDDEN);
        saleRepo.save(sp);
    }

}

package com.fpt.evplatform.modules.salepost.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.fpt.evplatform.modules.batterypost.dto.response.BatteryPostResponse;
import com.fpt.evplatform.modules.batterypost.entity.BatteryPost;
import com.fpt.evplatform.modules.inspectionreport.repository.InspectionReportRepository;
import com.fpt.evplatform.modules.media.dto.response.MediaResponse;
import com.fpt.evplatform.modules.media.entity.Media;
import com.fpt.evplatform.modules.salepost.dto.response.PostCard;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.PostCardProjection;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.vehiclepost.dto.response.VehiclePostResponse;
import com.fpt.evplatform.modules.vehiclepost.entity.VehiclePost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalePostQueryService {

    private final SalePostRepository salePostRepository;
    private final Cloudinary cloudinary;
    private final InspectionReportRepository  inspectionReportRepository;

    // ---------- LISTING ----------
    public Page<PostCard> listCards(Pageable pageable) {
        return salePostRepository.findCards(pageable).map(this::toCard);
    }

    public Page<PostCard> getMyPosts(String username, Pageable pageable) {
        return salePostRepository.findCardsByUsername(username, pageable).map(this::toCard);
    }

    public Page<PostCard> getVehiclePosts(Pageable pageable) {
        return salePostRepository.findVehiclePosts(pageable).map(this::toCard);
    }

    public Page<PostCard> getBatteryPosts(Pageable pageable) {
        return salePostRepository.findBatteryPosts(pageable).map(this::toCard);
    }


    private PostCard toCard(PostCardProjection p) {
        PostCard card = new PostCard();
        card.setListingId(p.getListingId());
        card.setProductName(p.getProductName());
        card.setAskPrice(p.getAskPrice());
        card.setProductType(p.getProductType());
        card.setProvinceCode(p.getProvinceCode());
        card.setDistrictCode(p.getDistrictCode());
        card.setWardCode(p.getWardCode());
        card.setStreet(p.getStreet());
        card.setStatus(p.getStatus());
        card.setPriorityLevel(p.getPriorityLevel());
        card.setSellerUsername(p.getSellerUsername());


        // Ghép address nhanh (nếu có LocationService thì map code -> tên)
        StringBuilder addr = new StringBuilder();
        if (p.getStreet() != null && !p.getStreet().isBlank()) addr.append(p.getStreet());
        if (p.getWardCode() != null)     addr.append(addr.length()>0?", ":"").append("Xã.").append(p.getWardCode());
        if (p.getDistrictCode() != null) addr.append(addr.length()>0?", ":"").append("Huyện.").append(p.getDistrictCode());
        if (p.getProvinceCode() != null) addr.append(addr.length()>0?", ":"").append("Tỉnh.").append(p.getProvinceCode());
        card.setAddress(addr.toString());

        String insp = p.getInspectionStatus();
        card.setInspected(insp != null && !insp.isBlank());
        card.setInspectionStatus(insp);

        // cover thumb (nếu có media)
        String publicId = p.getCoverPublicId();
        String type = p.getCoverType(); // IMAGE | VIDEO
        if (publicId != null && !publicId.isBlank()) {
            String resource = "VIDEO".equalsIgnoreCase(type) ? "video" : "image";
            Transformation thumb = new Transformation()
                    .width(320).height(320)
                    .crop("fill").gravity("auto")
                    .quality("auto").fetchFormat("auto");
            String urlThumb = cloudinary.url()
                    .resourceType(resource).secure(true)
                    .transformation(thumb)
                    .generate(publicId);
            card.setCoverThumb(urlThumb);
        }
        return card;
    }

    // ---------- DETAIL ----------
    public PostResponse getDetail(Integer listingId) {
        SalePost sp = salePostRepository.findByListingId(listingId)
                .orElseThrow(() -> new NoSuchElementException("SalePost not found"));

        PostResponse dto = new PostResponse();
        dto.setListingId(sp.getListingId());
        dto.setSellerUsername(sp.getSeller().getUsername());
        dto.setSellerPhone(sp.getSeller().getPhone());
        dto.setSellerAvatarUrl(sp.getSeller().getAvatarUrl());
        if (sp.getSeller().getAvatarPublicId() != null) {
            String thumb = cloudinary.url()
                    .resourceType("image")
                    .secure(true)
                    .transformation(new com.cloudinary.Transformation<>()
                            .width(256)
                            .height(256)
                            .crop("thumb")
                            .gravity("face")
                            .quality("auto")
                            .fetchFormat("auto"))
                    .generate(sp.getSeller().getAvatarPublicId());

            dto.setSellerAvatarThumbUrl(thumb);
        }
        dto.setSellerId(sp.getSeller().getUserId());
        dto.setProductType(sp.getProductType() != null ? sp.getProductType() : null);
        dto.setTitle(sp.getTitle());
        dto.setDescription(sp.getDescription());
        dto.setAskPrice(sp.getAskPrice());
        dto.setStatus(sp.getStatus());
        dto.setStatus(sp.getStatus() != null ? sp.getStatus(): null);
        dto.setProvinceCode(sp.getProvinceCode());
        dto.setDistrictCode(sp.getDistrictCode());
        dto.setWardCode(sp.getWardCode());
        dto.setStreet(sp.getStreet());
        dto.setPriorityLevel(sp.getPriorityLevel());
        dto.setCreatedAt(sp.getCreatedAt());

        // ==== map nested battery / vehicle ====
        if (sp.getBatteryPost() != null) {
            dto.setBatteryPost(toBatteryDto(sp.getBatteryPost()));
        }
        if (sp.getVehiclePost() != null) {
            dto.setVehiclePost(toVehicleDto(sp.getVehiclePost()));
        }

        String inspStatus = inspectionReportRepository.findLatestStatusByListingId(listingId);
        dto.setInspected(inspStatus != null && !inspStatus.isBlank());
        dto.setInspectionStatus(inspStatus);


        if (sp.getMediaList() != null) {
            dto.setMedia(sp.getMediaList().stream()
                    .map(this::toMediaDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private MediaResponse toMediaDto(Media m) {
        MediaResponse mr = new MediaResponse();
        mr.setMediaId(m.getMediaId());
        mr.setPublicId(m.getPublicId());
        mr.setUrl(m.getUrl());
        mr.setType(m.getType());        // "IMAGE"/"VIDEO"
        mr.setSortOrder(m.getSortOrder());

        String resource = "VIDEO".equalsIgnoreCase(m.getType()) ? "video" : "image";
        String publicId = m.getPublicId();

        // Large
        Transformation large = new Transformation()
                .width(1280).crop("limit").quality("auto").fetchFormat("auto");
        // Thumb
        Transformation thumb = new Transformation()
                .width(320).height(320).crop("fill").gravity("auto").quality("auto").fetchFormat("auto");

        mr.setUrlLarge(cloudinary.url()
                .resourceType(resource).secure(true)
                .transformation(large)
                .generate(publicId));

        mr.setUrlThumb(cloudinary.url()
                .resourceType(resource).secure(true)
                .transformation(thumb)
                .generate(publicId));

        return mr;
    }

    private BatteryPostResponse toBatteryDto(BatteryPost bp) {
        BatteryPostResponse dto = new BatteryPostResponse();
        // điền các field bạn có trong BatteryPostResponse
        dto.setChemistryName(bp.getChemistryName());
        dto.setCapacityKwh(bp.getCapacityKwh());
        dto.setSohPercent(bp.getSohPercent());
        dto.setCycleCount(bp.getCycleCount());
        // thêm id nếu DTO có
        // dto.setBatteryId(bp.getBatteryId());
        return dto;
    }

    private VehiclePostResponse toVehicleDto(VehiclePost vp) {
        VehiclePostResponse dto = new VehiclePostResponse();
        dto.setModelName(vp.getModel().getName());
        dto.setBrandName(vp.getModel().getBrand().getName());
        dto.setYear(vp.getYear());
        dto.setOdoKm(vp.getOdoKm());
        dto.setVin(vp.getVin());
        dto.setTransmission(vp.getTransmission());
        dto.setFuelType(vp.getFuelType());
        dto.setOrigin(vp.getOrigin());
        dto.setBodyStyle(vp.getBodyStyle());
        dto.setSeatCount(vp.getSeatCount());
        dto.setColor(vp.getColor());
        dto.setAccessories(vp.isAccessories());
        dto.setRegistration(vp.isRegistration());
        return dto;
    }
}

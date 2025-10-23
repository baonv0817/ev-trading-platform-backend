package com.fpt.evplatform.modules.salepost.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.fpt.evplatform.modules.media.dto.response.MediaResponse;
import com.fpt.evplatform.modules.media.entity.Media;
import com.fpt.evplatform.modules.salepost.dto.response.PostCard;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.PostCardProjection;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
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

    // ---------- LISTING ----------
    public Page<PostCard> listCards(Pageable pageable) {
        return salePostRepository.findCards(pageable).map(this::toCard);
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

        // Ghép address nhanh (nếu có LocationService thì map code -> tên)
        StringBuilder addr = new StringBuilder();
        if (p.getStreet() != null && !p.getStreet().isBlank()) addr.append(p.getStreet());
        if (p.getWardCode() != null)     addr.append(addr.length()>0?", ":"").append("W.").append(p.getWardCode());
        if (p.getDistrictCode() != null) addr.append(addr.length()>0?", ":"").append("D.").append(p.getDistrictCode());
        if (p.getProvinceCode() != null) addr.append(addr.length()>0?", ":"").append("P.").append(p.getProvinceCode());
        card.setAddress(addr.toString());

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
        dto.setSeller(sp.getSeller().getUsername());
        dto.setProductType(sp.getProductType() != null ? sp.getProductType() : null);
        dto.setTitle(sp.getTitle());
        dto.setDescription(sp.getDescription());
        dto.setAskPrice(sp.getAskPrice());
        dto.setStatus(sp.getStatus() != null ? sp.getStatus(): null);
        dto.setProvinceCode(sp.getProvinceCode());
        dto.setDistrictCode(sp.getDistrictCode());
        dto.setWardCode(sp.getWardCode());
        dto.setStreet(sp.getStreet());
        dto.setPriorityLevel(sp.getPriorityLevel());
        dto.setCreatedAt(sp.getCreatedAt());

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
}

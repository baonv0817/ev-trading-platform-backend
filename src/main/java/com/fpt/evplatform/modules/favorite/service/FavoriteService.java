package com.fpt.evplatform.modules.favorite.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.favorite.dto.request.FavoriteRequest;
import com.fpt.evplatform.modules.favorite.dto.response.FavoriteResponse;
import com.fpt.evplatform.modules.favorite.entity.Favorite;
import com.fpt.evplatform.modules.favorite.entity.FavoriteId;
import com.fpt.evplatform.modules.favorite.mapper.FavoriteMapper;
import com.fpt.evplatform.modules.favorite.repository.FavoriteRepository;
import com.fpt.evplatform.modules.salepost.dto.response.PostCard;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.PostCardProjection;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteService {
    FavoriteRepository favoriteRepository;
    UserRepository userRepository;
    SalePostRepository salePostRepository;
    FavoriteMapper favoriteMapper;
    Cloudinary cloudinary;

    public FavoriteResponse addFavorite(FavoriteRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        SalePost listing = salePostRepository.findById(request.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));

        if (favoriteRepository.existsByUserAndListing(user, listing)) {
            throw new AppException(ErrorCode.FAVORITE_ALREADY_EXISTS);
        }

        Favorite favorite = favoriteMapper.toFavorite(request, user, listing);
        return favoriteMapper.toFavoriteResponse(favoriteRepository.save(favorite));
    }

    public void removeFavorite(FavoriteRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        SalePost listing = salePostRepository.findById(request.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));

        Favorite favorite = favoriteRepository.findByUserAndListing(user, listing)
                .orElseThrow(() -> new AppException(ErrorCode.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favorite);
    }


    public Page<PostCard> getUserFavorites(Integer userId,  Pageable pageable) {
        return salePostRepository.findFavoriteCardsByUserId(userId, pageable).map(this::toCard);

    }

    public boolean isFavorited(Integer userId, Integer listingId) {
        return favoriteRepository.existsById(new FavoriteId(userId, listingId));
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
        card.setPriorityLevel(p.getPriorityLevel());

        // Ghép address nhanh (nếu có LocationService thì map code -> tên)
        StringBuilder addr = new StringBuilder();
        if (p.getStreet() != null && !p.getStreet().isBlank()) addr.append(p.getStreet());
        if (p.getWardCode() != null)     addr.append(addr.length()>0?", ":"").append("Xã.").append(p.getWardCode());
        if (p.getDistrictCode() != null) addr.append(addr.length()>0?", ":"").append("Huyện.").append(p.getDistrictCode());
        if (p.getProvinceCode() != null) addr.append(addr.length()>0?", ":"").append("Tỉnh.").append(p.getProvinceCode());
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
}

package com.fpt.evplatform.modules.favorite.service;

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

}

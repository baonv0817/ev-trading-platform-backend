package com.fpt.evplatform.modules.favorite.mapper;

import com.fpt.evplatform.modules.favorite.dto.request.FavoriteRequest;
import com.fpt.evplatform.modules.favorite.dto.response.FavoriteResponse;
import com.fpt.evplatform.modules.favorite.entity.Favorite;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.favorite.entity.FavoriteId;
import com.fpt.evplatform.modules.user.entity.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface FavoriteMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "listing", source = "listing")
    @Mapping(
            target = "favoriteId",
            expression = "java(new com.fpt.evplatform.modules.favorite.entity.FavoriteId(" +
                    "user.getUserId(), listing.getListingId()))"
    )
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Favorite toFavorite(FavoriteRequest req, User user, SalePost listing);
    
    FavoriteResponse toFavoriteResponse(Favorite favorite);
}

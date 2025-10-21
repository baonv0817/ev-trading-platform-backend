package com.fpt.evplatform.modules.favorite.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.favorite.dto.request.FavoriteRequest;
import com.fpt.evplatform.modules.favorite.dto.response.FavoriteResponse;
import com.fpt.evplatform.modules.favorite.service.FavoriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FavoriteController {
    FavoriteService favoriteService;

    @PostMapping
    public ApiResponse<FavoriteResponse> addFavorite(@RequestBody FavoriteRequest request) {
        return ApiResponse.<FavoriteResponse>builder()
                .result(favoriteService.addFavorite(request))
                .message("Added to favorites successfully")
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> removeFavorite(@RequestBody FavoriteRequest request) {
        favoriteService.removeFavorite(request);
        return ApiResponse.<Void>builder()
                .message("Removed from favorites successfully")
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<FavoriteResponse>> getUserFavorites(@PathVariable Integer userId) {
        return ApiResponse.<List<FavoriteResponse>>builder()
                .result(favoriteService.getUserFavorites(userId))
                .message("User favorites retrieved successfully")
                .build();
    }

    @GetMapping("/check")
    public ApiResponse<Boolean> checkFavorite(@RequestParam Integer userId, @RequestParam Integer listingId) {
        boolean exists = favoriteService.isFavorited(userId, listingId);
        return ApiResponse.<Boolean>builder()
                .result(exists)
                .build();
    }
}

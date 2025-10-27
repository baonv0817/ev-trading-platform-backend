package com.fpt.evplatform.modules.favorite.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.favorite.dto.request.FavoriteRequest;
import com.fpt.evplatform.modules.favorite.dto.response.FavoriteResponse;
import com.fpt.evplatform.modules.favorite.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Favorite listings", description = "Mark favorite listings for users")
public class FavoriteController {
    FavoriteService favoriteService;

    @Operation(summary = "Add a favorite listing")
    @PostMapping
    public ApiResponse<FavoriteResponse> addFavorite(@RequestBody FavoriteRequest request) {
        return ApiResponse.<FavoriteResponse>builder()
                .result(favoriteService.addFavorite(request))
                .message("Added to favorites successfully")
                .build();
    }

    @Operation(summary = "Remove a favorite listing")
    @DeleteMapping
    public ApiResponse<Void> removeFavorite(@RequestBody FavoriteRequest request) {
        favoriteService.removeFavorite(request);
        return ApiResponse.<Void>builder()
                .message("Removed from favorites successfully")
                .build();
    }

    @Operation(summary = "Get a User's favorite list")
    @GetMapping("/user/{userId}")
    public ApiResponse<List<FavoriteResponse>> getUserFavorites(@PathVariable Integer userId) {
        return ApiResponse.<List<FavoriteResponse>>builder()
                .result(favoriteService.getUserFavorites(userId))
                .message("User favorites retrieved successfully")
                .build();
    }

    @Operation(summary = "Check if a listing is favorite by a User")
    @GetMapping("/check")
    public ApiResponse<Boolean> checkFavorite(@RequestParam Integer userId, @RequestParam Integer listingId) {
        boolean exists = favoriteService.isFavorited(userId, listingId);
        return ApiResponse.<Boolean>builder()
                .result(exists)
                .build();
    }
}

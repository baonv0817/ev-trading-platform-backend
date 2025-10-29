package com.fpt.evplatform.modules.review.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.review.dto.request.ReviewRequest;
import com.fpt.evplatform.modules.review.dto.response.ReviewResponse;
import com.fpt.evplatform.modules.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Reviews", description = "Manage buyer/seller reviews after deals")
public class ReviewController {

    ReviewService reviewService;

    @Operation(summary = "Create a new review")
    @PostMapping
    public ApiResponse<ReviewResponse> createReview(@RequestBody ReviewRequest req) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReview(req))
                .message("Review created successfully")
                .build();
    }

    @Operation(summary = "Get reviews received by a user")
    @GetMapping("/target/{userId}")
    public ApiResponse<List<ReviewResponse>> getReviewsByUser(@PathVariable Integer userId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByUser(userId))
                .build();
    }

    @Operation(summary = "Get reviews written by a user")
    @GetMapping("/author/{userId}")
    public ApiResponse<List<ReviewResponse>> getReviewsByAuthor(@PathVariable Integer userId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByAuthor(userId))
                .build();
    }

    @Operation(summary = "Get reviews related to a deal")
    @GetMapping("/deal/{dealId}")
    public ApiResponse<List<ReviewResponse>> getReviewsByDeal(@PathVariable Integer dealId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByDeal(dealId))
                .build();
    }

    @Operation(summary = "(ADMIN) Delete a review")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.<Void>builder()
                .message("Review deleted successfully")
                .build();
    }
}

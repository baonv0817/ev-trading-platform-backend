package com.fpt.evplatform.modules.offer.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.offer.dto.request.OfferRequest;
import com.fpt.evplatform.modules.offer.dto.response.OfferResponse;
import com.fpt.evplatform.modules.offer.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Offers", description = "Manage offers between buyers and sellers")
@SecurityRequirement(name = "bearerAuth")
public class OfferController {

    OfferService offerService;

    @Operation(summary = "Create a new Offer")
    @PostMapping
    public ApiResponse<OfferResponse> createOffer(@RequestBody OfferRequest req) {
        return ApiResponse.<OfferResponse>builder()
                .result(offerService.createOffer(req))
                .message("Offer submitted successfully")
                .build();
    }

    @Operation(summary = "Get list of offers from a User (buyer)")
    @GetMapping("/buyer/{buyerId}")
    public ApiResponse<List<OfferResponse>> getOffersByBuyer(@PathVariable Integer buyerId) {
        return ApiResponse.<List<OfferResponse>>builder()
                .result(offerService.getOffersByBuyer(buyerId))
                .build();
    }

    @Operation(summary = "Get list of offers for a Listing")
    @GetMapping("/listing/{listingId}")
    public ApiResponse<List<OfferResponse>> getOffersByListing(@PathVariable Integer listingId) {
        return ApiResponse.<List<OfferResponse>>builder()
                .result(offerService.getOffersByListing(listingId))
                .build();
    }

    @Operation(summary = "Get list of offers received by a Seller")
    @GetMapping("/seller/{sellerId}")
    public ApiResponse<List<OfferResponse>> getOffersBySeller(@PathVariable Integer sellerId) {
        return ApiResponse.<List<OfferResponse>>builder()
                .result(offerService.getOffersBySeller(sellerId))
                .build();
    }

    @Operation(summary = "Update status of an Offer")
    @PutMapping("/{offerId}/status")
    public ApiResponse<OfferResponse> updateOfferStatus(
            @PathVariable Integer offerId,
            @RequestParam String status) {
        return ApiResponse.<OfferResponse>builder()
                .result(offerService.updateOfferStatus(offerId, status))
                .message("Offer status updated successfully")
                .build();
    }

    @Operation(summary = "Delete an Offer")
    @DeleteMapping("/{offerId}")
    public ApiResponse<Void> deleteOffer(@PathVariable Integer offerId) {
        offerService.deleteOffer(offerId);
        return ApiResponse.<Void>builder()
                .message("Offer deleted successfully")
                .build();
    }
}

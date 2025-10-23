package com.fpt.evplatform.modules.offer.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.offer.dto.request.OfferRequest;
import com.fpt.evplatform.modules.offer.dto.response.OfferResponse;
import com.fpt.evplatform.modules.offer.service.OfferService;
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
public class OfferController {

    OfferService offerService;

    @PostMapping
    public ApiResponse<OfferResponse> createOffer(@RequestBody OfferRequest req) {
        return ApiResponse.<OfferResponse>builder()
                .result(offerService.createOffer(req))
                .message("Offer submitted successfully")
                .build();
    }

    @GetMapping("/buyer/{buyerId}")
    public ApiResponse<List<OfferResponse>> getOffersByBuyer(@PathVariable Integer buyerId) {
        return ApiResponse.<List<OfferResponse>>builder()
                .result(offerService.getOffersByBuyer(buyerId))
                .build();
    }

    @GetMapping("/listing/{listingId}")
    public ApiResponse<List<OfferResponse>> getOffersByListing(@PathVariable Integer listingId) {
        return ApiResponse.<List<OfferResponse>>builder()
                .result(offerService.getOffersByListing(listingId))
                .build();
    }

    @PutMapping("/{offerId}/status")
    public ApiResponse<OfferResponse> updateOfferStatus(
            @PathVariable Integer offerId,
            @RequestParam String status) {
        return ApiResponse.<OfferResponse>builder()
                .result(offerService.updateOfferStatus(offerId, status))
                .message("Offer status updated successfully")
                .build();
    }

    @DeleteMapping("/{offerId}")
    public ApiResponse<Void> deleteOffer(@PathVariable Integer offerId) {
        offerService.deleteOffer(offerId);
        return ApiResponse.<Void>builder()
                .message("Offer deleted successfully")
                .build();
    }
}

package com.fpt.evplatform.modules.deal.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.common.enums.DealStatus;
import com.fpt.evplatform.modules.deal.dto.request.DealRequest;
import com.fpt.evplatform.modules.deal.dto.response.DealResponse;
import com.fpt.evplatform.modules.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.fpt.evplatform.modules.deal.dto.response.CreateCheckoutResponse;
import java.util.List;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Deals", description = "Manage deals between buyers and sellers")
@SecurityRequirement(name = "bearerAuth")
public class DealController {

    DealService dealService;

    @Operation(summary = "Create new deal after offer accepted")
    @PostMapping
    public ApiResponse<DealResponse> createDeal(@RequestBody DealRequest req) {
        return ApiResponse.<DealResponse>builder()
                .result(dealService.createDeal(req))
                .message("Deal created successfully")
                .build();
    }

    @Operation(summary = "Assign a platform site and schedule a deal")
    @PutMapping("/{dealId}/assign-site")
    public ApiResponse<DealResponse> assignSite(@PathVariable Integer dealId, @RequestBody DealRequest req) {
        return ApiResponse.<DealResponse>builder()
                .result(dealService.assignPlatformSite(dealId, req))
                .message("Platform site assigned successfully")
                .build();
    }

    @Operation(summary = "Mark a deal as completed (observer/admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{dealId}/complete")
    public ApiResponse<DealResponse> completeDeal(@PathVariable Integer dealId) {
        return ApiResponse.<DealResponse>builder()
                .result(dealService.completeDeal(dealId))
                .message("Deal marked as completed")
                .build();
    }

    @Operation(summary = "(ADMIN) Get all deals or filter by status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ApiResponse<List<DealResponse>> getAllDeals(
            @RequestParam(required = false) DealStatus status) {
        List<DealResponse> deals = (status == null)
                ? dealService.getAllDeals()
                : dealService.getDealsByStatus(status);
        return ApiResponse.<List<DealResponse>>builder()
                .result(deals)
                .build();
    }

    @Operation(summary = "Update deal status manually")
    @PutMapping("/{dealId}/status")
    public ApiResponse<DealResponse> updateStatus(
            @PathVariable Integer dealId,
            @RequestParam DealStatus status) {
        return ApiResponse.<DealResponse>builder()
                .result(dealService.updateStatus(dealId, status))
                .message("Deal status updated to " + status)
                .build();
    }

    @Operation(summary = "Delete a deal (ADMIN)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{dealId}")
    public ApiResponse<Void> deleteDeal(@PathVariable Integer dealId) {
        dealService.deleteDeal(dealId);
        return ApiResponse.<Void>builder()
                .message("Deal deleted successfully")
                .build();
    }

    @Operation(summary = "Get all deals for a specific buyer")
    @GetMapping("/buyer/{buyerId}")
    public ApiResponse<List<DealResponse>> getDealsByBuyer(@PathVariable Integer buyerId) {
        return ApiResponse.<List<DealResponse>>builder()
                .result(dealService.getDealsByBuyer(buyerId))
                .build();
    }

    @Operation(summary = "Get all deals for a specific seller")
    @GetMapping("/seller/{sellerId}")
    public ApiResponse<List<DealResponse>> getDealsBySeller(@PathVariable Integer sellerId) {
        return ApiResponse.<List<DealResponse>>builder()
                .result(dealService.getDealsBySeller(sellerId))
                .build();
    }

    // 2. Lấy URL thanh toán Stripe
    @PostMapping("/{dealId}/checkout")
    public ResponseEntity<CreateCheckoutResponse> createCheckout(@PathVariable Integer dealId) throws Exception {
        return ResponseEntity.ok(dealService.createCheckout(dealId));
    }


    @PostMapping("/{dealId}/confirm")
    public ResponseEntity<Void> confirmPayment(@PathVariable Integer dealId) {
        dealService.confirmPayment(dealId);
        return ResponseEntity.ok().build();
    }

}

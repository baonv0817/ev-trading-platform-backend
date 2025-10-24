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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Deals", description = "Manage deals between buyers and sellers")
@SecurityRequirement(name = "bearerAuth")
public class DealController {

    DealService dealService;

    @Operation(summary = "Create new deal from accepted offer")
    @PostMapping
    public ApiResponse<DealResponse> createDeal(@RequestBody DealRequest req) {
        return ApiResponse.<DealResponse>builder()
                .result(dealService.createDeal(req))
                .message("Deal created successfully")
                .build();
    }

    @Operation(summary = "Get all deals (admin)")
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

    @Operation(summary = "Update deal status (admin or seller)")
    @PutMapping("/{dealId}/status")
    public ApiResponse<DealResponse> updateStatus(
            @PathVariable Integer dealId,
            @RequestParam DealStatus status) {
        return ApiResponse.<DealResponse>builder()
                .result(dealService.updateStatus(dealId, status))
                .message("Deal status updated to " + status)
                .build();
    }

    @Operation(summary = "Delete deal (admin)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{dealId}")
    public ApiResponse<Void> deleteDeal(@PathVariable Integer dealId) {
        dealService.deleteDeal(dealId);
        return ApiResponse.<Void>builder()
                .message("Deal deleted successfully")
                .build();
    }
}

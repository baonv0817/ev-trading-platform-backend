package com.fpt.evplatform.modules.escrow.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.escrow.dto.response.EscrowResponse;
import com.fpt.evplatform.modules.escrow.service.EscrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/escrows")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Escrows", description = "Manage deal escrow payments")
public class EscrowController {

    EscrowService escrowService;

    @Operation(summary = "Get escrow by deal ID")
    @GetMapping("/deal/{dealId}")
    public ApiResponse<EscrowResponse> getEscrowByDealId(@PathVariable Integer dealId) {
        return ApiResponse.<EscrowResponse>builder()
                .result(escrowService.getEscrowByDealId(dealId))
                .build();
    }

    @Operation(summary = "Release escrow after deal completion (admin or observer)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/deal/{dealId}/release")
    public ApiResponse<EscrowResponse> releaseEscrow(@PathVariable Integer dealId) {
        return ApiResponse.<EscrowResponse>builder()
                .result(escrowService.releaseEscrow(dealId))
                .message("Escrow released successfully")
                .build();
    }

    @Operation(summary = "Cancel escrow for a failed/cancelled deal (admin only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/deal/{dealId}/cancel")
    public ApiResponse<Void> cancelEscrow(@PathVariable Integer dealId) {
        escrowService.cancelEscrow(dealId);
        return ApiResponse.<Void>builder()
                .message("Escrow cancelled successfully")
                .build();
    }
}

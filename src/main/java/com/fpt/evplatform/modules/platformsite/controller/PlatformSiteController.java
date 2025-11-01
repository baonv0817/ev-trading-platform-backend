package com.fpt.evplatform.modules.platformsite.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.platformsite.dto.response.PlatformSiteResponse;
import com.fpt.evplatform.modules.platformsite.service.PlatformSiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platform-sites")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Available Platform Sites", description = "List of ACTIVE Platform Sites")
public class PlatformSiteController {
    PlatformSiteService platformSiteService;

    @Operation(summary = "List of ACTIVE Platform Sites")
    @GetMapping("/active")
    public ApiResponse<List<PlatformSiteResponse>> getActiveSites() {
        return ApiResponse.<List<PlatformSiteResponse>>builder()
                .result(platformSiteService.getActiveSites())
                .build();
    }
}

package com.fpt.evplatform.modules.platformsite.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.platformsite.dto.response.PlatformSiteResponse;
import com.fpt.evplatform.modules.platformsite.service.PlatformSiteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platform-sites")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlatformSiteController {
    PlatformSiteService platformSiteService;

    @GetMapping("/active")
    public ApiResponse<List<PlatformSiteResponse>> getActiveSites() {
        return ApiResponse.<List<PlatformSiteResponse>>builder()
                .result(platformSiteService.getActiveSites())
                .build();
    }
}

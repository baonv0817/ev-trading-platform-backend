package com.fpt.evplatform.modules.platformsite.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.platformsite.dto.request.PlatformSiteRequest;
import com.fpt.evplatform.modules.platformsite.dto.response.PlatformSiteResponse;
import com.fpt.evplatform.modules.platformsite.service.PlatformSiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/platform-sites")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Platform Sites Control", description = "Basic CRUD for platform sites")
public class PlatformSiteAdminController {

    PlatformSiteService platformSiteService;

    @Operation(summary = "List all Platform Sites")
    @GetMapping
    public ApiResponse<List<PlatformSiteResponse>> getAllSites() {
        return ApiResponse.<List<PlatformSiteResponse>>builder()
                .result(platformSiteService.getAllSites())
                .build();
    }

    @Operation(summary = "Find Platform Site by ID")
    @GetMapping("/{id}")
    public ApiResponse<PlatformSiteResponse> getSiteById(@PathVariable Integer id) {
        return ApiResponse.<PlatformSiteResponse>builder()
                .result(platformSiteService.getSiteById(id))
                .build();
    }

    @Operation(summary = "Add a new Platform Site")
    @PostMapping
    public ApiResponse<PlatformSiteResponse> createSite(@RequestBody PlatformSiteRequest req) {
        return ApiResponse.<PlatformSiteResponse>builder()
                .result(platformSiteService.createSite(req))
                .build();
    }

    @Operation(summary = "Update an existing Platform Site")
    @PutMapping("/{id}")
    public ApiResponse<PlatformSiteResponse> updateSite(@PathVariable Integer id, @RequestBody PlatformSiteRequest req) {
        return ApiResponse.<PlatformSiteResponse>builder()
                .result(platformSiteService.updateSite(id, req))
                .build();
    }

    @Operation(summary = "Delete a Platform Site")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSite(@PathVariable Integer id) {
        platformSiteService.deleteSite(id);
        return ApiResponse.<Void>builder()
                .message("Platform site deleted successfully")
                .build();
    }
}

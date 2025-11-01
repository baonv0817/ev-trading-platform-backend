package com.fpt.evplatform.modules.brand.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.brand.dto.request.BrandRequest;
import com.fpt.evplatform.modules.brand.dto.response.BrandResponse;
import com.fpt.evplatform.modules.brand.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Brand (Admin)", description = "Admin-only operations for managing brands")
@SecurityRequirement(name = "bearerAuth")
public class BrandAdminController {

    BrandService brandService;

    @Operation(summary = "(ADMIN) Add a new Brand")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<BrandResponse> createBrand(@RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.createBrand(request))
                .message("Brand created successfully")
                .build();
    }

    @Operation(summary = "(ADMIN) Update a Brand")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable Integer id, @RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.updateBrand(id, request))
                .message("Brand updated successfully")
                .build();
    }

    @Operation(summary = "(ADMIN) Delete a Brand")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<Void> deleteBrandById(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
        return ApiResponse.<Void>builder()
                .message("Brand deleted successfully")
                .build();
    }
}

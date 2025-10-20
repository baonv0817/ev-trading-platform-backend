package com.fpt.evplatform.modules.brand.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.brand.dto.request.BrandRequest;
import com.fpt.evplatform.modules.brand.dto.response.BrandResponse;
import com.fpt.evplatform.modules.brand.entity.Brand;
import com.fpt.evplatform.modules.brand.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/brands")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BrandController {
    BrandService brandService;

    @GetMapping
    public ApiResponse<List<BrandResponse>> findAll() {
        return ApiResponse.<List<BrandResponse>>builder()
                .result(brandService.getAllBrands())
                .message("Brands retrieved successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandResponse> findById(@PathVariable Integer id) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.getBrandById(id))
                .message("Brand retrieved successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<BrandResponse> createBrand(@RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.createBrand(request))
                .message("Brand created successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable Integer id, @RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.updateBrand(id, request))
                .message("Brand updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBrandById(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
        return ApiResponse.<Void>builder()
                .message("Brand deleted successfully")
                .build();
    }


}

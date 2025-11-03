package com.fpt.evplatform.modules.brand.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.brand.dto.response.BrandResponse;
import com.fpt.evplatform.modules.brand.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Brand (Public)", description = "Public endpoints for viewing brands")
public class BrandController {

    BrandService brandService;

    @Operation(summary = "List all Brands (Public)")
    @GetMapping
    public ApiResponse<List<BrandResponse>> findAll() {
        return ApiResponse.<List<BrandResponse>>builder()
                .result(brandService.getAllBrands())
                .message("Brands retrieved successfully")
                .build();
    }

    @Operation(summary = "Find Brand by ID (Public)")
    @GetMapping("/{id}")
    public ApiResponse<BrandResponse> findById(@PathVariable Integer id) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.getBrandById(id))
                .message("Brand retrieved successfully")
                .build();
    }
}

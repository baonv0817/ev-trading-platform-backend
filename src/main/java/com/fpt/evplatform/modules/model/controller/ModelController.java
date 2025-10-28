package com.fpt.evplatform.modules.model.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.model.dto.request.ModelRequest;
import com.fpt.evplatform.modules.model.dto.response.ModelResponse;
import com.fpt.evplatform.modules.model.entity.Model;
import com.fpt.evplatform.modules.model.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/models")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Model", description = "CRUD operations for models")
public class ModelController {
    ModelService modelService;

    @Operation(summary = "List all Models")
    @GetMapping
    public ApiResponse<List<ModelResponse>> findAll() {
        return ApiResponse.<List<ModelResponse>>builder()
                .result(modelService.getAllModels())
                .message("Models retrieved successfully")
                .build();
    }

    @Operation(summary = "Find Model by ID")
    @GetMapping("/{id}")
    public ApiResponse<ModelResponse> findById(@PathVariable Integer id) {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.getModelById(id))
                .message("Model retrieved successfully")
                .build();
    }

    @Operation(summary = "Find Models by Brand")
    @GetMapping("/brand/{brandId}")
    public ApiResponse<List<ModelResponse>> findByBrand(@PathVariable Integer brandId) {
        return ApiResponse.<List<ModelResponse>>builder()
                .result(modelService.getModelsByBrand(brandId))
                .message("Models retrieved successfully for brand " + brandId)
                .build();
    }

    @Operation(summary = "(ADMIN) Create a new Model")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ApiResponse<ModelResponse> createModel(@RequestBody ModelRequest request) {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.createModel(request))
                .message("Model created successfully")
                .build();
    }

    @Operation(summary = "(ADMIN) Update a Model")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<ModelResponse> updateModel(@PathVariable Integer id, @RequestBody ModelRequest request) {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.updateModel(id, request))
                .message("Model updated successfully")
                .build();
    }

    @Operation(summary = "(ADMIN) Delete a Model")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteModel(@PathVariable Integer id) {
        modelService.deleteModel(id);
        return ApiResponse.<Void>builder()
                .message("Model deleted successfully")
                .build();
    }
}

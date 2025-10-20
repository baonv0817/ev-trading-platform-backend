package com.fpt.evplatform.modules.model.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.model.dto.request.ModelRequest;
import com.fpt.evplatform.modules.model.dto.response.ModelResponse;
import com.fpt.evplatform.modules.model.entity.Model;
import com.fpt.evplatform.modules.model.service.ModelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/models")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ModelController {
    ModelService modelService;

    @GetMapping
    public ApiResponse<List<ModelResponse>> findAll() {
        return ApiResponse.<List<ModelResponse>>builder()
                .result(modelService.getAllModels())
                .message("Models retrieved successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ModelResponse> findById(@PathVariable Integer id) {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.getModelById(id))
                .message("Model retrieved successfully")
                .build();
    }

    @GetMapping("/brand/{brandId}")
    public ApiResponse<List<ModelResponse>> findByBrand(@PathVariable Integer brandId) {
        return ApiResponse.<List<ModelResponse>>builder()
                .result(modelService.getModelsByBrand(brandId))
                .message("Models retrieved successfully for brand " + brandId)
                .build();
    }

    @PostMapping
    public ApiResponse<ModelResponse> createModel(@RequestBody ModelRequest request) {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.createModel(request))
                .message("Model created successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ModelResponse> updateModel(@PathVariable Integer id, @RequestBody ModelRequest request) {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.updateModel(id, request))
                .message("Model updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteModel(@PathVariable Integer id) {
        modelService.deleteModel(id);
        return ApiResponse.<Void>builder()
                .message("Model deleted successfully")
                .build();
    }
}

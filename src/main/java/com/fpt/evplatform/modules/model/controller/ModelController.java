package com.fpt.evplatform.modules.model.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.model.dto.response.ModelResponse;
import com.fpt.evplatform.modules.model.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/models")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Model (Public)", description = "Public endpoints for viewing models")
public class ModelController {

    ModelService modelService;

    @Operation(summary = "List all Models (Public)")
    @GetMapping
    public ApiResponse<List<ModelResponse>> findAll() {
        return ApiResponse.<List<ModelResponse>>builder()
                .result(modelService.getAllModels())
                .message("Models retrieved successfully")
                .build();
    }

    @Operation(summary = "Find Model by ID (Public)")
    @GetMapping("/{id}")
    public ApiResponse<ModelResponse> findById(@PathVariable Integer id) {
        return ApiResponse.<ModelResponse>builder()
                .result(modelService.getModelById(id))
                .message("Model retrieved successfully")
                .build();
    }

    @Operation(summary = "Find Models by Brand (Public)")
    @GetMapping("/brand/{brandId}")
    public ApiResponse<List<ModelResponse>> findByBrand(@PathVariable Integer brandId) {
        return ApiResponse.<List<ModelResponse>>builder()
                .result(modelService.getModelsByBrand(brandId))
                .message("Models retrieved successfully for brand " + brandId)
                .build();
    }
}

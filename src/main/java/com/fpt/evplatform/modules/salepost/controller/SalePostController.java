package com.fpt.evplatform.modules.salepost.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.salepost.dto.request.CreatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.service.SalePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sale-posts")
@RequiredArgsConstructor
public class SalePostController {

    private final SalePostService service;

    @PostMapping()
    ApiResponse<PostResponse> create(@AuthenticationPrincipal(expression = "subject") String username, @Valid @RequestBody CreatePostRequest req) {
        return ApiResponse.<PostResponse>builder()
                .result(service.createPost(username, req))
                .build();
    }
}
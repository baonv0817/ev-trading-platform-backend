package com.fpt.evplatform.modules.salepost.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.salepost.dto.request.CreatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.response.PostCard;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.service.SalePostQueryService;
import com.fpt.evplatform.modules.salepost.service.SalePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sale-posts")
@RequiredArgsConstructor
public class SalePostController {

    private final SalePostService service;

    private final SalePostQueryService salePostQueryService;


    @PostMapping()
    ApiResponse<PostResponse> create(@AuthenticationPrincipal(expression = "subject") String username, @Valid @RequestBody CreatePostRequest req) {
        return ApiResponse.<PostResponse>builder()
                .result(service.createPost(username, req))
                .build();
    }

    @GetMapping
    public Page<PostCard> list(Pageable pageable) {
        return salePostQueryService.listCards(pageable);
    }

    // Detail: trả full media[]
    @GetMapping("/{listingId}")
    public PostResponse getDetail(@PathVariable Integer listingId) {
        return salePostQueryService.getDetail(listingId);
    }
}
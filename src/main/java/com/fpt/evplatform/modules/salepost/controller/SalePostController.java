package com.fpt.evplatform.modules.salepost.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.salepost.dto.request.CreatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.request.UpdatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.response.PostCard;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.service.SalePostQueryService;
import com.fpt.evplatform.modules.salepost.service.SalePostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sale-posts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SalePostController {

    private final SalePostService service;
    private final SalePostQueryService salePostQueryService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<PostResponse> create(@AuthenticationPrincipal(expression = "subject") String username, @RequestPart("payload") @Valid String payload,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            CreatePostRequest req = objectMapper.readValue(payload, CreatePostRequest.class);
            return ApiResponse.<PostResponse>builder()
                    .result(service.createPost(username, req, files))
                    .build();
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Page<PostCard> getMyPosts(@AuthenticationPrincipal(expression = "subject") String username, Pageable pageable) {
        return salePostQueryService.getMyPosts(username, pageable);
    }

    @Operation(security = {})
    @GetMapping
    public Page<PostCard> list(Pageable pageable) {
        return salePostQueryService.listCards(pageable);
    }

    @Operation(security = {})
    // Detail: trả full media[]
    @GetMapping("/{listingId}")
    public PostResponse getDetail(@PathVariable Integer listingId) {
        return salePostQueryService.getDetail(listingId);
    }

    @PatchMapping("/{listingId}")
    @PreAuthorize("@postAuth.canModifySalePost(authentication, #listingId)")
    public PostResponse update(@PathVariable Integer listingId,
                               @RequestBody @Valid UpdatePostRequest req) {
        return service.update(listingId, req);
    }

    @DeleteMapping("/{listingId}")
    @PreAuthorize("@postAuth.canModifySalePost(authentication, #listingId)")
    public void delete(@PathVariable Integer listingId) {
        service.delete(listingId);
    }
}
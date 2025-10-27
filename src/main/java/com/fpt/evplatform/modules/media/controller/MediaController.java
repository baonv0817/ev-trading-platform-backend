package com.fpt.evplatform.modules.media.controller;


import com.fpt.evplatform.modules.media.dto.response.MediaResponse;
import com.fpt.evplatform.modules.media.service.MediaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sale-posts/{listingId}/media")
@SecurityRequirement(name = "bearerAuth")
public class MediaController {

    private final MediaService mediaService;

    @GetMapping
    public List<MediaResponse> list(@PathVariable Integer listingId) {
        return mediaService.listByListing(listingId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@postAuth.canModifySalePost(authentication, #listingId)")
    public List<MediaResponse> upload(
            @PathVariable Integer listingId,
            @RequestPart("files") @NotEmpty List<MultipartFile> files
    ) {
        return mediaService.upload(listingId, files);
    }

    @DeleteMapping("/{mediaId}")
    @PreAuthorize("@postAuth.canModifySalePost(authentication, #listingId)")
    public void delete(@PathVariable Integer listingId, @PathVariable Integer mediaId) {
        mediaService.delete(listingId, mediaId);
    }

    @PatchMapping("/reorder")
    @PreAuthorize("@postAuth.canModifySalePost(authentication, #listingId)")
    public void reorder(@PathVariable Integer listingId, @RequestBody List<Integer> orderedIds) {
        mediaService.reorder(listingId, orderedIds);
    }
}

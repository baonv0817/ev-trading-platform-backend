package com.fpt.evplatform.modules.user.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.modules.user.dto.response.AvatarResponse;
import com.fpt.evplatform.modules.user.service.UserAvatarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserAvatarController {

    private final UserAvatarService avatarService;

    @PostMapping(path = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<AvatarResponse> uploadAvatar(
            @AuthenticationPrincipal(expression = "subject") String username,
            @RequestPart("file") MultipartFile file
    ) {
        AvatarResponse res = avatarService.uploadMyAvatar(username, file);
        return ApiResponse.<AvatarResponse>builder().result(res).build();
    }

    @DeleteMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> deleteAvatar(
            @AuthenticationPrincipal(expression = "subject") String username
    ) {
        avatarService.deleteMyAvatar(username);
        return ApiResponse.<Void>builder().build();
    }
}
package com.fpt.evplatform.modules.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvatarResponse {
    private String publicId;
    private String url;
    private String urlThumb;
}
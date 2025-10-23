package com.fpt.evplatform.modules.media.dto.response;


import lombok.Data;

@Data
public class MediaResponse {
    private Integer mediaId;
    private String publicId;
    private String type;
    private String url;
    private String urlLarge;
    private String urlThumb;
    private Integer sortOrder;
}

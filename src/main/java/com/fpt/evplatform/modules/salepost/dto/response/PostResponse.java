package com.fpt.evplatform.modules.salepost.dto.response;

import com.fpt.evplatform.common.enums.PostStatus;
import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.modules.batterypost.dto.response.BatteryPostResponse;
import com.fpt.evplatform.modules.media.dto.response.MediaResponse;
import com.fpt.evplatform.modules.vehiclepost.dto.response.VehiclePostResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {

    private Integer listingId;
    private String seller;
    private ProductType productType;
    private BigDecimal askPrice;
    private String title;
    private String description;
    private PostStatus status;
    private Integer provinceCode;
    private Integer districtCode;
    private Integer wardCode;
    private String street;
    private Integer priorityLevel;
    private LocalDateTime createdAt;

    private BatteryPostResponse batteryPost;
    private VehiclePostResponse vehiclePost;

    private List<MediaResponse> media;

}



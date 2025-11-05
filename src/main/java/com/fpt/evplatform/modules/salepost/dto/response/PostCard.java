package com.fpt.evplatform.modules.salepost.dto.response;

import com.fpt.evplatform.common.enums.PostStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PostCard {
    private Integer listingId;
    private String  productName;   // từ sp.title
    private BigDecimal askPrice;
    private String  productType;
    private Integer priorityLevel;

    private Integer provinceCode;
    private Integer districtCode;
    private Integer wardCode;
    private String  street;
    private PostStatus status;
    private String  address;
    private String  coverThumb;

    private String sellerUsername;
}

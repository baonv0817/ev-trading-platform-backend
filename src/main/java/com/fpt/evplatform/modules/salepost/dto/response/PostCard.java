package com.fpt.evplatform.modules.salepost.dto.response;

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

    private String  address;       // chuỗi gộp nhanh: street, W., D., P. (tuỳ bạn thay bằng tên)
    private String  coverThumb;    // URL 320x320
}

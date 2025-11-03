package com.fpt.evplatform.modules.favorite.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteResponse {
    Integer listingId;
    String  productName;   // từ sp.title
    BigDecimal askPrice;
    String  productType;
    Integer priorityLevel;
    Integer provinceCode;
    Integer districtCode;
    Integer wardCode;
    String  street;
    String  address;       // chuỗi gộp nhanh: street, W., D., P. (tuỳ bạn thay bằng tên)
    String  coverThumb;    // URL 320x320
}

package com.fpt.evplatform.modules.inspectionorder.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {
    Integer listingId;
    LocalDateTime scheduledAt;
    Integer provinceCode;
    Integer districtCode;
    Integer wardCode;
    String street;
    BigDecimal price;
}

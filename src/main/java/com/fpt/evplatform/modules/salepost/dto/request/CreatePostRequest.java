package com.fpt.evplatform.modules.salepost.dto.request;

import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePostRequest {

    @NotNull
    private ProductType productType;

    @NotNull private BigDecimal askPrice;
    @NotNull private Integer provinceCode;
    private Integer districtCode;
    private Integer wardCode;
    private String street;
    @Size(max = 580) private String description;

    @Valid
    private BatteryDetail battery;
//    @Valid private VehicleDetail vehicle;

    @Data
    public static class BatteryDetail {
        private String chemistryName;
        @NotNull @Positive
        private Double capacityKwh;
        @NotNull @Min(0) @Max(100) private Integer sohPercent;
        @NotNull @Min(0) private Integer cycleCount;
    }

//    @Data
//    public static class VehicleDetail {
//        private String brand;
//        private String model;
//        @NotNull @Min(1900) private Integer year;
//        @NotNull @Min(0) private Long mileageKm;
//        @NotNull @Min(0) @Max(100) private Integer batteryHealthPct;
//    }
}
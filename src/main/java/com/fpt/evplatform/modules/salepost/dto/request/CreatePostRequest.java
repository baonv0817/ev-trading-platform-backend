package com.fpt.evplatform.modules.salepost.dto.request;

import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.modules.model.entity.Model;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePostRequest {

    @NotNull
    private ProductType productType;
    private String title;
    @NotNull private BigDecimal askPrice;
    @NotNull private Integer provinceCode;
    private Integer districtCode;
    private Integer wardCode;
    private String street;
    @Size(max = 580) private String description;

    @Valid
    private BatteryDetail battery;
   @Valid private VehicleDetail vehicle;

    @Data
    public static class BatteryDetail {
        private String chemistryName;
        @NotNull @Positive
        private Double capacityKwh;
        @NotNull @Min(0) @Max(100) private Integer sohPercent;
        @NotNull @Min(0) private Integer cycleCount;
    }

    @Data
    public static class VehicleDetail {
        private Integer modelId;
        private Integer year;
        private Integer odoKm;
        private String vin;
        private String transmission;
        private String fuelType;
        private String origin;
        private String bodyStyle;
        private Integer seatCount;
        private String color;
        private boolean accessories;
        private boolean registration;
    }
}
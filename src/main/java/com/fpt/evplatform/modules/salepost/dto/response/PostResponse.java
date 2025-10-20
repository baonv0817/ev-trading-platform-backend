package com.fpt.evplatform.modules.salepost.dto.response;

import com.fpt.evplatform.common.enums.PostStatus;
import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.modules.batterypost.entity.BatteryPost;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PostResponse {

    private Integer listingId;
    private Integer sellerId;
    private ProductType productType;
    private BigDecimal askPrice;
    private String description;
    private PostStatus status;
    private Integer provinceCode;
    private Integer districtCode;
    private Integer wardCode;
    private String street;
    private Integer priorityLevel;
    private LocalDateTime createdAt;

    private BatteryPost batteryPost;
//    private Vehicle vehicle;

//    @Data
//    public static class Battery {
//        private Long batteryId;
//        private String chemistryName;
//        private Double capacityKwh;
//        private Integer sohPercent;
//        private Integer cycleCount;
//    }

//    @Data
//    public static class Vehicle {
//        private Long vehicleId;
//        private String brand;
//        private String model;
//        private Integer year;
//        private Long mileageKm;
//        private Integer batteryHealthPct;
//    }
}



package com.fpt.evplatform.modules.ai.dto.request;


import com.fpt.evplatform.common.enums.ProductType;
import lombok.Data;

@Data
public class AiPriceRequest {
    private ProductType productType; // VEHICLE | BATTERY
    private Integer provinceCode;

    // Vehicle
    private String brand;
    private String model;
    private Integer year;
    private Integer odoKm;

    // Battery
    private String chemistryName;   // LFP/NMC/...
    private Double capacityKwh;     // 42.0
    private Integer sohPercent;     // 0..100
    private Integer cycleCount;     // >=0
}
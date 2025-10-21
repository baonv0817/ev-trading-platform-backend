package com.fpt.evplatform.modules.batterypost.dto.response;

import lombok.Data;

@Data
public class BatteryPostResponse {
    private String chemistryName;
    private Double capacityKwh;
    private Integer sohPercent;
    private Integer cycleCount;
}

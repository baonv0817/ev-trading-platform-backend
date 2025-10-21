package com.fpt.evplatform.modules.ai.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AiPriceResponse {
    private BigDecimal priceMinVND;
    private BigDecimal priceMaxVND;
    private BigDecimal suggestedPriceVND;
    private String note;
}
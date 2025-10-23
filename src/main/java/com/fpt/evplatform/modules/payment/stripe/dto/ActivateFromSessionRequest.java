package com.fpt.evplatform.modules.payment.stripe.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActivateFromSessionRequest {
    @NotBlank
    private String sessionId;
}
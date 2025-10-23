package com.fpt.evplatform.modules.payment.stripe.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCheckoutRequest {
    @NotNull
    private Integer planId;
}

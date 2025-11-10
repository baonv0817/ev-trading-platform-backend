package com.fpt.evplatform.modules.deal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCheckoutResponse {
    String sessionId;
    String url;
}
package com.fpt.evplatform.modules.inspectionorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCheckoutResponse {
    String sessionId;
    String url;
}
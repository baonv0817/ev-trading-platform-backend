package com.fpt.evplatform.modules.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelResponse {
    Integer modelId;
    String name;
    Integer brandId;
    String brandName;
}

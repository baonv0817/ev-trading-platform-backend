package com.fpt.evplatform.modules.platformsite.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlatformSiteResponse {
    Integer platformSiteId;
    String name;
    Integer provinceCode;
    Integer districtCode;
    Integer wardCode;
    String street;
    Boolean active;
}

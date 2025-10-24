package com.fpt.evplatform.modules.platformsite.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlatformSiteRequest {
    String name;
    Integer provinceCode;
    Integer districtCode;
    Integer wardCode;
    String street;
    Boolean active;
}
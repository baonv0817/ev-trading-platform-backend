package com.fpt.evplatform.modules.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleUserDTO {
    Integer userId;
    String username;
    String fullName;
    String phone;
}

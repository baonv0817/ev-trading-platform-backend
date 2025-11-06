package com.fpt.evplatform.modules.user.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateRequest {
    String firstName;
    String lastName;

    @Column(unique = true)
    String email;
    @Column(unique = true)
    String phone;

    Integer provinceCode;
    Integer districtCode;
    Integer wardCode;
    String bio;
}

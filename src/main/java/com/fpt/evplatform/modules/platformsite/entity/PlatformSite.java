package com.fpt.evplatform.modules.platformsite.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "platform_sites")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlatformSite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int platformSiteId;

    String name;
    int provinceCode;
    int districtCode;
    int wardCode;
    String street;
    boolean active;

}

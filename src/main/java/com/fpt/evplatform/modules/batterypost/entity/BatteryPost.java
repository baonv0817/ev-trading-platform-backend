package com.fpt.evplatform.modules.batterypost.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "battery_posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatteryPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer batteryId;

    String chemistryName;
    Double capacityKwh;
    Integer sohPercent;
    Integer cycleCount;
}

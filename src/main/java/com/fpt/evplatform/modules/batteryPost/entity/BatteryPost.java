package com.fpt.evplatform.modules.batteryPost.entity;

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
    int batteryId;

    String chemicalName;
    double capacityKwh;
    int sohPercent;
    int cycleCount;
}

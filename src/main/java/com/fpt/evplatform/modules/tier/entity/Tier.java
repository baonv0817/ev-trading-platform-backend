package com.fpt.evplatform.modules.tier.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tiers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int tierId;

    @Column(unique = true, nullable = false)
    String code;

    String name;

    int durationDays;

    double price;

    int feedWeight;

}

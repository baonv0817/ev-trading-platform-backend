package com.fpt.evplatform.modules.membership.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "membership_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer planId;
    String name;
    String description;
    Integer durationDays;
    String currency;
    BigDecimal price;
    Integer maxPosts;
    Integer priorityLevel;
}

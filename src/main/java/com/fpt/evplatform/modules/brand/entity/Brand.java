package com.fpt.evplatform.modules.brand.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "brands")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int brandId;

    @Column(unique = true, nullable = false)
    String name;
}

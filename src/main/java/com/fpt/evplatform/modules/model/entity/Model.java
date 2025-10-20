package com.fpt.evplatform.modules.model.entity;

import com.fpt.evplatform.modules.brand.entity.Brand;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "models")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer modelId;

    String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    Brand brand;
}

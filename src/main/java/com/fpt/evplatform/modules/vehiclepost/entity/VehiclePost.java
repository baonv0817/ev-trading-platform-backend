package com.fpt.evplatform.modules.vehiclepost.entity;

import com.fpt.evplatform.modules.model.entity.Model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "vehicle_posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehiclePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer vehicleId;

    @ManyToOne
    @JoinColumn(name = "model_id")
    Model model;

    Integer year;
    Integer odoKm;
    String vin;
    String transmission;
    String fuelType;
    String origin;
    String bodyStyle;
    int seatCount;
    String color;
    boolean accessories;
    boolean registration;
}

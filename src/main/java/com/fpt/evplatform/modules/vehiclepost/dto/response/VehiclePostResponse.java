package com.fpt.evplatform.modules.vehiclepost.dto.response;

import com.fpt.evplatform.modules.model.entity.Model;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehiclePostResponse {
    String modelName;
    String brandName;
    Integer year;
    Integer odoKm;
    String vin;
    String transmission;
    String fuelType;
    String origin;
    String bodyStyle;
    Integer seatCount;
    String color;
    boolean accessories;
    boolean registration;
}

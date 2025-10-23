package com.fpt.evplatform.modules.salepost.entity;

import com.fpt.evplatform.common.enums.PostStatus;
import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.modules.batterypost.entity.BatteryPost;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.vehiclepost.entity.VehiclePost;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sale_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer listingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;

    @Enumerated(EnumType.STRING)
    ProductType productType;

    String description;

    BigDecimal askPrice;

    @Enumerated(EnumType.STRING)
    PostStatus status;

    Integer provinceCode;

    Integer districtCode;

    Integer wardCode;

    String street;

    Integer priorityLevel;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "battery_id")
    BatteryPost batteryPost;

   @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumn(name = "vehicle_id")
   VehiclePost vehiclePost;

    @PrePersist
    void prePersist() {
        var now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) status = PostStatus.ACTIVE;
    }

    @PreUpdate
    void preUpdate() { updatedAt = LocalDateTime.now(); }
}

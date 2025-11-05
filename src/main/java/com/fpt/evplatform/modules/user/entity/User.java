package com.fpt.evplatform.modules.user.entity;

import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    @Column(unique = true, nullable = false)
    String username;

    @Column(unique = true)
    String email;

    String password;

    @Column(unique = true)
    String phone;

    String firstName;
    String lastName;
    BigDecimal wallet;
    Integer provinceCode;
    Integer districtCode;
    Integer wardCode;

    String bio;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    MembershipPlan plan;

    String avatarUrl;
    String avatarPublicId;

    String planStatus;
    String role;
    LocalDateTime startAt;
    LocalDateTime endAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

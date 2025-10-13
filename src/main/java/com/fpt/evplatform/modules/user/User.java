package com.fpt.evplatform.modules.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
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
    @Column(nullable = false)
    Integer planId;
    String planStatus;
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

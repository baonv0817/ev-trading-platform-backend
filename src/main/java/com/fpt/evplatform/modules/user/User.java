package com.fpt.evplatform.modules.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDate;

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
    Integer id;

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
    Timestamp startAt;
    Timestamp endAt;
    Timestamp createdAt;
    Timestamp updatedAt;
}

package com.fpt.evplatform.modules.review.entity;

import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"deal_id", "author_user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    Integer reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false)
    Deal deal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id")
    User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    User target;

    @Column(name = "rating")
    Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    String comment;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}

package com.fpt.evplatform.modules.review.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    Integer reviewId;
    Integer dealId;
    Integer authorId;
    String authorName;
    Integer targetId;
    String targetName;
    Integer rating;
    String comment;
    LocalDateTime createdAt;
}

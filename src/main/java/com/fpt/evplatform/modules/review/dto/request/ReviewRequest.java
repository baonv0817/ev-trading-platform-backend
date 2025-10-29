package com.fpt.evplatform.modules.review.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    Integer dealId;
    Integer authorId;
    Integer targetId;
    Integer rating;
    String comment;
}

package com.fpt.evplatform.modules.favorite.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteResponse {
    Integer userId;
    String username;
    Integer listingId;
    String productType;
    String description;
    Double askPrice;
    LocalDateTime createdAt;
}

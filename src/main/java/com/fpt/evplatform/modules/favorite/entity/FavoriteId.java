package com.fpt.evplatform.modules.favorite.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class FavoriteId implements Serializable {
    private Integer userId;
    private Integer listingId;
}


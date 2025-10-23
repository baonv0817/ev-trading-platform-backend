package com.fpt.evplatform.modules.media.entity;

import com.fpt.evplatform.modules.salepost.entity.SalePost;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "media")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer mediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    SalePost salePost;

    String url;

    String publicId;

    String type;

    Integer sortOrder;
}

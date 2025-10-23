package com.fpt.evplatform.modules.salepost.repository;

import com.fpt.evplatform.modules.salepost.entity.SalePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SalePostRepository extends JpaRepository<SalePost, Integer> {
    @Query(
            value = """
        SELECT
          sp.listing_id        AS listingId,
          sp.title             AS productName,
          sp.ask_price         AS askPrice,
          sp.product_type      AS productType,
          sp.province_code     AS provinceCode,
          sp.district_code     AS districtCode,
          sp.ward_code         AS wardCode,
          sp.street            AS street,
          (
            SELECT m.public_id
            FROM media m
            WHERE m.listing_id = sp.listing_id
            ORDER BY m.sort_order ASC
            LIMIT 1
          ) AS coverPublicId,
          (
            SELECT m.type
            FROM media m
            WHERE m.listing_id = sp.listing_id
            ORDER BY m.sort_order ASC
            LIMIT 1
          ) AS coverType
        FROM sale_posts sp
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM sale_posts sp
        """,
            nativeQuery = true
    )
    Page<PostCardProjection> findCards(Pageable pageable);

    // Detail: load luôn media + seller để tránh N+1
    @EntityGraph(attributePaths = {"mediaList", "seller"})
    Optional<SalePost> findByListingId(Integer listingId);

    Integer countBySeller_UserIdAndCreatedAtBetween(Integer sellerId, LocalDateTime start, LocalDateTime end);
}

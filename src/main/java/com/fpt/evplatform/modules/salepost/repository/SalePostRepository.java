package com.fpt.evplatform.modules.salepost.repository;

import com.fpt.evplatform.modules.salepost.entity.SalePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SalePostRepository extends JpaRepository<SalePost, Integer> {
    Integer countBySellerIdAndCreatedAtBetween(Integer sellerId, LocalDateTime start, LocalDateTime end);
}

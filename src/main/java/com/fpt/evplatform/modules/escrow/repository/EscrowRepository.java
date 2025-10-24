package com.fpt.evplatform.modules.escrow.repository;

import com.fpt.evplatform.modules.escrow.entity.Escrow;
import com.fpt.evplatform.modules.deal.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EscrowRepository extends JpaRepository<Escrow, Integer> {
    Optional<Escrow> findByDeal(Deal deal);
    Optional<Escrow> findByDeal_DealId(Integer dealId);
}

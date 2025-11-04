package com.fpt.evplatform.modules.deal.repository;

import com.fpt.evplatform.common.enums.DealStatus;
import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealRepository extends JpaRepository<Deal,Integer> {
    List<Deal> findByStatus(DealStatus status);
    Optional<Deal> findByOffer(Offer offer);

    @Query("SELECT d FROM Deal d WHERE d.offer.buyer = :buyer")
    List<Deal> findByBuyer(User buyer);

    @Query("SELECT d FROM Deal d WHERE d.offer.listing.seller = :seller")
    List<Deal> findBySeller(User seller);
}

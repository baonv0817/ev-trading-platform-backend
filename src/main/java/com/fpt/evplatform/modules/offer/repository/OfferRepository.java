package com.fpt.evplatform.modules.offer.repository;

import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    List<Offer> findByBuyer(User buyer);
    List<Offer> findByListing(SalePost listing);
    Optional<Offer> findByBuyerAndListing(User buyer, SalePost listing);

}

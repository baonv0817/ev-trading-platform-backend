package com.fpt.evplatform.modules.review.repository;

import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.review.entity.Review;
import com.fpt.evplatform.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Optional<Review> findByDealAndAuthor(Deal deal, User author);

    List<Review> findByTarget(User target);

    List<Review> findByAuthor(User author);

    List<Review> findByDeal(Deal deal);
}

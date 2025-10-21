package com.fpt.evplatform.modules.favorite.repository;

import com.fpt.evplatform.modules.favorite.entity.Favorite;
import com.fpt.evplatform.modules.favorite.entity.FavoriteId;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndListing(User user, SalePost listing);
    boolean existsByUserAndListing(User user, SalePost listing);
}

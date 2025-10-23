package com.fpt.evplatform.modules.media.repository;

import com.fpt.evplatform.modules.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    List<Media> findBySalePost_ListingIdOrderBySortOrderAsc(Integer listingId);
}

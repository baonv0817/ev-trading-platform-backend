package com.fpt.evplatform.modules.platformsite.repository;

import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformSiteRepository extends JpaRepository<PlatformSite,Integer> {
    Optional<PlatformSite> findFirstByActiveTrue();
    List<PlatformSite> findAllByActiveTrue();
}

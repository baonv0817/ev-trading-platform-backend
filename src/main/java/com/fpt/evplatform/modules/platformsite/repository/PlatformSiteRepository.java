package com.fpt.evplatform.modules.platformsite.repository;

import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformSiteRepository extends JpaRepository<PlatformSite,Integer> {

}

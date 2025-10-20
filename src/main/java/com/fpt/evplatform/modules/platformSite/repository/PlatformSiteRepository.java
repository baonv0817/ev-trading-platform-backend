package com.fpt.evplatform.modules.platformSite.repository;

import com.fpt.evplatform.modules.platformSite.entity.PlatformSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformSiteRepository extends JpaRepository<PlatformSite,Integer> {

}

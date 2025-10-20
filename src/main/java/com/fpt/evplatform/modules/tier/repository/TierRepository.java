package com.fpt.evplatform.modules.tier.repository;

import com.fpt.evplatform.modules.tier.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepository extends JpaRepository<Tier,Integer> {

}

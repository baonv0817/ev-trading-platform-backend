package com.fpt.evplatform.modules.membership.repository;

import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Integer> {
    boolean existsByName(String name);
    Optional<MembershipPlan> findByName(String name);
}

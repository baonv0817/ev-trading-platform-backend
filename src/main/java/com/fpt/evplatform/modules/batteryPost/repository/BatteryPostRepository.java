package com.fpt.evplatform.modules.batteryPost.repository;

import com.fpt.evplatform.modules.batteryPost.entity.BatteryPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryPostRepository extends JpaRepository<BatteryPost, Integer> {

}

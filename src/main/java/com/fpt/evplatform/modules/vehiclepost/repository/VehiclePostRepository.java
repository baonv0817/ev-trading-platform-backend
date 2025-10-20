package com.fpt.evplatform.modules.vehiclepost.repository;

import com.fpt.evplatform.modules.model.entity.Model;
import com.fpt.evplatform.modules.vehiclepost.entity.VehiclePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclePostRepository extends JpaRepository<VehiclePost, Integer> {
    List<VehiclePost> findByModel(Model model);

}

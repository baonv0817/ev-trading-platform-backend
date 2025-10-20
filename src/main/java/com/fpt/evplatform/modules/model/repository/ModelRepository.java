package com.fpt.evplatform.modules.model.repository;

import com.fpt.evplatform.modules.brand.entity.Brand;
import com.fpt.evplatform.modules.model.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
    List<Model> findByBrand(Brand brand);
    Optional<Model> findByNameIgnoreCase(String name);
}

package com.example.first.repository;

import com.example.first.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    List<Feature> findByParentIsNull();

    Optional<Feature> findByName(String name);
}

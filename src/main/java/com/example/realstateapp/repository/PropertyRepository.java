package com.example.realstateapp.repository;

import com.example.realstateapp.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// PropertyRepository.java
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findBySubwayStation(String subwayStation);
}

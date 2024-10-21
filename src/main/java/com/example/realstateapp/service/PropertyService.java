package com.example.realstateapp.service;

import com.example.realstateapp.model.Property;
import com.example.realstateapp.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> findPropertiesBySubwayStation(String subwayStation) {
        return propertyRepository.findSubwayStation( subwayStation );
    }

    public Property save(Property property) {
        return propertyRepository.save( property );
    }
}

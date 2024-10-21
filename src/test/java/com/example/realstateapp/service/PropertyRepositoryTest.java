package com.example.realstateapp.service;

import com.example.realstateapp.model.Property;
import com.example.realstateapp.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PropertyRepositoryTest {

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    public void testFindBySubwayStation() {
        List<Property> properties = propertyRepository.findSubwayStation("경기광주역");
        assertFalse(properties.isEmpty()); // 결과가 비어 있지 않은지 확인
    }
}
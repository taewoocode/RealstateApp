package com.example.realstateapp.controller;
import com.example.realstateapp.model.Property;
import com.example.realstateapp.service.KakaoMapService;
import com.example.realstateapp.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final KakaoMapService kakaoMapService;

    public PropertyController(PropertyService propertyService, KakaoMapService kakaoMapService) {
        this.propertyService = propertyService;
        this.kakaoMapService = kakaoMapService;
    }

    @GetMapping("/subway")
    public ResponseEntity<List<Property>> getPropertiesBySubwayStation(@RequestParam String subwayStation) {
        // 지하철역의 위치 정보를 가져옴
        String locationInfo = kakaoMapService.getLocationInfo(subwayStation);
        if (locationInfo == null) {
            return ResponseEntity.notFound().build(); // 위치 정보를 찾을 수 없는 경우
        }

        // 위도, 경도를 분리
        String[] latLong = locationInfo.split(",");
        double latitude = Double.parseDouble(latLong[0]);
        double longitude = Double.parseDouble(latLong[1]);

        // 해당 위치 근처의 매물 리스트를 찾음
        List<Property> properties = propertyService.findPropertiesBySubwayStation(subwayStation);

        return ResponseEntity.ok(properties);
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.save(property);
        return ResponseEntity.ok(savedProperty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property propertyDetails) {
        Property updatedProperty = propertyService.update(id, propertyDetails);
        return updatedProperty != null ? ResponseEntity.ok(updatedProperty) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.findAll();
        return ResponseEntity.ok(properties);
    }
}


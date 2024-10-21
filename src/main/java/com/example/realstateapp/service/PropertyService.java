package com.example.realstateapp.service;

import com.example.realstateapp.model.Property;
import com.example.realstateapp.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final KakaoMapService kakaoMapService;

    public PropertyService(PropertyRepository propertyRepository, KakaoMapService kakaoMapService) {
        this.propertyRepository = propertyRepository;
        this.kakaoMapService = kakaoMapService;
    }

    //클라이언트가 입력한 지하철역을 검색하고, 근처 매물을 찾음
    public List<Property> findPropertiesBySubwayStation(String subwayStation) {
        String location = kakaoMapService.getLocationInfo(subwayStation);
        if (location != null) {
            String[] latLng = location.split(",");
            double latitude = Double.parseDouble(latLng[0]);
            return findPropertiesByLocation(latitude, Double.parseDouble(latLng[1]) );
        }
        return new ArrayList<>(); // 위치 정보를 찾을 수 없는 경우 빈 리스트 반환
    }

    private List<Property> findPropertiesByLocation(double latitude, double longitude) {
        List<Property> allProperties = propertyRepository.findAll();
        List<Property> nearbyProperties = new ArrayList<>();

        for (Property property : allProperties) {
            double propertyLat = property.getLatitude();
            double propertyLng = property.getLongitude();

            // 두 위치 간의 거리 계산 (예: 1km 이내의 매물만 추가)
            double distance = calculateDistance(latitude, longitude, propertyLat, propertyLng);
            if (distance <= 1.0) {
                nearbyProperties.add(property);
            }
        }
        return nearbyProperties;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // 지구 반지름 (킬로미터)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // 킬로미터 반환
    }

    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    public Property update(Long id, Property propertyDetails) {
        Optional<Property> optionalProperty = propertyRepository.findById(id);
        if (optionalProperty.isPresent()) {
            Property property = optionalProperty.get();
            property.setTitle(propertyDetails.getTitle());
            property.setDescription(propertyDetails.getDescription());
            property.setPrice(propertyDetails.getPrice());
            property.setRegion(propertyDetails.getRegion());
            property.setSubwayStation(propertyDetails.getSubwayStation());
            property.setLatitude(propertyDetails.getLatitude());
            property.setLongitude(propertyDetails.getLongitude());
            return propertyRepository.save(property);
        }
        return null; // 또는 예외 처리
    }

    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).orElse(null); // 또는 예외 처리
    }

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }
}

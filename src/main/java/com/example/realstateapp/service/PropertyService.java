package com.example.realstateapp.service;

import com.example.realstateapp.model.Property;
import com.example.realstateapp.model.dto.PropertyDto;
import com.example.realstateapp.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    public List<Property> findPropertiesBySubwayStation(String subwayStation) {
        String location = kakaoMapService.getLocationInfo(subwayStation);
        if (location != null) {
            String[] latLng = location.split(",");
            double latitude = Double.parseDouble(latLng[0]);
            double longitude = Double.parseDouble(latLng[1]);
            return findPropertiesByLocation(latitude, longitude);
        }
        return new ArrayList<>();
    }

    // 위도와 경도에 근접한 매물을 찾음
    private List<Property> findPropertiesByLocation(double latitude, double longitude) {
        List<Property> allProperties = propertyRepository.findAll();
        List<Property> nearbyProperties = new ArrayList<>();
        for (Property property : allProperties) {
            if (calculateDistance(latitude, longitude, property.getLatitude(), property.getLongitude()) <= 1.0) {
                nearbyProperties.add(property);
            }
        }
        return nearbyProperties;
    }

    // 두 지점 간 거리 계산
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // 지구 반지름 (킬로미터)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    // 매물 저장
    public PropertyDto save(PropertyDto propertyDto, MultipartFile imageFile) {
        Property property = convertToEntity(propertyDto);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadImage(imageFile);
            property.setImageUrl(imageUrl); // 이미지 URL 저장
        }

        Property savedProperty = propertyRepository.save(property);
        return convertToDto(savedProperty);
    }

    // 매물 업데이트
    public Property update(Long id, Property propertyDetails) {
        return propertyRepository.findById(id)
                .map(existingProperty -> {
                    updatePropertyDetails(existingProperty, propertyDetails);
                    return propertyRepository.save(existingProperty);
                })
                .orElseThrow(() -> new RuntimeException("Property not found")); // 예외 처리
    }

    // 매물 삭제
    public void delete(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property not found");
        }
        propertyRepository.deleteById(id);
    }

    // 특정 매물 조회
    public Property findById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    // 전체 매물 조회
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    // DTO -> Entity 변환
    private Property convertToEntity(PropertyDto propertyDto) {
        Property property = new Property();
        property.setTitle(propertyDto.getTitle());
        property.setPrice(String.valueOf(propertyDto.getPrice()));
        property.setLatitude(propertyDto.getLatitude());
        property.setLongitude(propertyDto.getLongitude());
        return property;
    }

    // Entity -> DTO 변환
    private PropertyDto convertToDto(Property property) {
        PropertyDto propertyDto = new PropertyDto();
        propertyDto.setTitle(property.getTitle());
        propertyDto.setPrice(Double.parseDouble(property.getPrice()));
        propertyDto.setLatitude(property.getLatitude());
        propertyDto.setLongitude(property.getLongitude());
        return propertyDto;
    }

    // 매물 상세 정보 업데이트
    private void updatePropertyDetails(Property existingProperty, Property propertyDetails) {
        existingProperty.setTitle(propertyDetails.getTitle());
        existingProperty.setDescription(propertyDetails.getDescription());
        existingProperty.setPrice(propertyDetails.getPrice());
        existingProperty.setRegion(propertyDetails.getRegion());
        existingProperty.setSubwayStation(propertyDetails.getSubwayStation());
        existingProperty.setLatitude(propertyDetails.getLatitude());
        existingProperty.setLongitude(propertyDetails.getLongitude());
    }

    // 이미지 업로드 (예: 로컬 또는 S3)
    private String uploadImage(MultipartFile file) {
        String uploadDir = "/path/to/upload/dir/"; // 실제 경로로 변경
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            file.transferTo(new File(uploadDir + fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e); // 예외 처리 강화
        }
        return "/uploads/" + fileName; // URL 반환
    }
}

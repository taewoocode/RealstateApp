package com.example.realstateapp.service;

        import com.example.realstateapp.model.Property;
        import com.example.realstateapp.repository.PropertyRepository;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;

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

    // 매물 수정
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

    // 매물 찾기 (단일)
    public Property findById(Long id) {
        return propertyRepository.findById(id).orElse(null); // 또는 예외 처리
    }

    // 모든 매물 찾기
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }
}

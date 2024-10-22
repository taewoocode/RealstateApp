package com.example.realstateapp.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@Getter @Setter
public class PropertyDto {
    private Long id;
    private String title;
    private Double price;
    private String location;
    private double latitude;
    private double longitude;

    public PropertyDto(Long id, String title, Double price, String location, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

package com.example.realstateapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 매물 제목
    private String description; // 매물 설명
    private String price; // 가격
    private String region; // 지역
    private String subwayStation; // 지하철역 이름
    private double latitude; // 위도
    private double longitude; // 경도

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

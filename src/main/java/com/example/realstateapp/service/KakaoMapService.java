package com.example.realstateapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoMapService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kakao.api.key}") // application.yml 또는 application.properties에 카카오 API 키 설정 필요
    private String kakaoApiKey;

    public KakaoMapService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    public String getLocationInfo(String query) {
        // 카카오맵 API URL
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";

        // URI 구성
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", query)
                .build()
                .toUriString();

        // HTTP 헤더에 API 키 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

        // 결과 반환
        return response.getBody();
    }
}

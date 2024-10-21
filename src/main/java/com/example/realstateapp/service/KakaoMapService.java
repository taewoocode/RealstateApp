package com.example.realstateapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

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

    public String getLocationInfo(String subwayStation) {
        // 카카오맵 API URL
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";

        // URI 구성
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", subwayStation + " 지하철역") // 지하철역 검색
                .build()
                .toUriString();

        // HTTP 헤더에 API 키 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

        // JSON 파싱 및 위치 정보 추출
        try {
            // JSON 문자열을 Map으로 변환
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> documents = (List<Map<String, Object>>) responseBody.get("documents");

            if (!documents.isEmpty()) {
                // 첫 번째 결과에서 위도, 경도 추출
                double latitude = (double) documents.get(0).get("y"); // 위도
                double longitude = (double) documents.get(0).get("x"); // 경도
                return latitude + "," + longitude; // 예시: 위도,경도 형식으로 반환
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }

        return null; // 위치 정보를 찾을 수 없는 경우
    }
}

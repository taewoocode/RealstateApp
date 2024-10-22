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

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public KakaoMapService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getLocationInfo(String subwayStation) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", subwayStation + " 지하철역")
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
                List<Map<String, Object>> documents = (List<Map<String, Object>>) responseBody.get("documents");

                if (!documents.isEmpty()) {
                    double latitude = (double) documents.get(0).get("y");
                    double longitude = (double) documents.get(0).get("x");
                    return latitude + "," + longitude;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}

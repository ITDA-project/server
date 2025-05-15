package com.itda.moamoa.domain.payment.service;

import com.itda.moamoa.global.config.PortOneProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PortOneApiClient {

    private final PortOneProperties portOneProperties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken";
        
        String apiKey = portOneProperties.getApiKey();
        String apiSecret = portOneProperties.getApiSecret();
        System.out.println("Using API Key: " + apiKey);
        System.out.println("Using API Secret: " + apiSecret);
        
        if (apiKey == null || apiKey.isEmpty() || apiSecret == null || apiSecret.isEmpty()) {
            throw new IllegalStateException("포트원 API 키 또는 시크릿 키가 설정되지 않았습니다.");
        }

        // 직접 JSON 문자열 생성
        String jsonBody = String.format("{\"imp_key\":\"%s\",\"imp_secret\":\"%s\"}", apiKey, apiSecret);
        System.out.println("Request body: " + jsonBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // 응답 본문 출력
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("아임포트 토큰 요청 실패: " + response.getBody());
        }

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("response").get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("토큰 파싱 실패", e);
        }
    }

    public Map<String, Object> getPaymentInfo(String impUid) {
        String accessToken = getAccessToken();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode responseNode = json.get("response");

            Map<String, Object> result = new HashMap<>();
            result.put("imp_uid", responseNode.get("imp_uid").asText());
            result.put("merchant_uid", responseNode.get("merchant_uid").asText());
            result.put("amount", responseNode.get("amount").asInt());

            return result;
        } catch (Exception e) {
            throw new RuntimeException("결제 정보 파싱 실패", e);
        }
    }

    public void requestRefund(String impUid, int amount) {
        String accessToken = getAccessToken();
        String url = "https://api.iamport.kr/payments/cancel";

        Map<String, Object> body = new HashMap<>();
        body.put("imp_uid", impUid);
        body.put("amount", amount); // 부분 환불 가능

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("환불 요청 실패: " + response.getBody());
        }
    }
}

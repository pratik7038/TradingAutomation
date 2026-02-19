package com.pratik.tradingAutomation.upstox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UpstoxClient {

    @Value("${upstox.access-token}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getLtp(String instrumentKey) {
        String url = "https://api.upstox.com/v3/market-quote/ltp?instrument_key=" + instrumentKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}

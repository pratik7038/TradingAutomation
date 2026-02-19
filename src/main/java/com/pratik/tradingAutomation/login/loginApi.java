package com.pratik.tradingAutomation.login;

import com.pratik.tradingAutomation.config.ApplicationProperties;
import com.pratik.tradingAutomation.upstox.AuthorizationCodeStore;
import com.upstox.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/login")
public class loginApi {

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private ApiClient apiClient;
    @Autowired
    private AuthorizationCodeStore authorizationCodeStore;

    @GetMapping("/authorization/dialog")
    public ResponseEntity<Object> authorizationDialog(@RequestParam(value = "state", required = false) String state) {
        String finalState = (state == null || state.isBlank()) ? UUID.randomUUID().toString() : state;
        authorizationCodeStore.setLatestState(finalState);

        String redirectUrl = UriComponentsBuilder
                .fromUriString("https://api.upstox.com/v2/login/authorization/dialog")
                .queryParam("response_type", "code")
                .queryParam("client_id", applicationProperties.getUpstoxClientId())
                .queryParam("redirect_uri", applicationProperties.getUpstoxRedirectUri())
                .queryParam("state", finalState)
                .build(true)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(java.net.URI.create(redirectUrl));
        ResponseEntity<Object> result = ResponseEntity.status(302).headers(headers).build();
        return result;
    }

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> token(@RequestParam(value = "code", required = false) String code) {
        String finalCode = (code == null || code.isBlank())
                ? authorizationCodeStore.getLatestCode()
                .orElseThrow(() -> new IllegalStateException("Missing authorization code. Login first and call callback, or pass ?code=..."))
                : code;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", finalCode);
        form.add("client_id", applicationProperties.getUpstoxClientId());
        form.add("client_secret", applicationProperties.getUpstoxClientSecret());
        form.add("redirect_uri", applicationProperties.getUpstoxRedirectUri());
        form.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.upstox.com/v2/login/authorization/token",
                entity,
                String.class
        );
        authorizationCodeStore.setLatestCode(response.getBody());
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }


}

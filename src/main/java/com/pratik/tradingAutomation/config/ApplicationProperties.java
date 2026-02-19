package com.pratik.tradingAutomation.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApplicationProperties {

    @Value("${trading.automation.access-token}")
    private String accessToken;

    @Value("${upstox.oauth.client-id:}")
    private String upstoxClientId;

    @Value("${upstox.oauth.client-secret:}")
    private String upstoxClientSecret;

    @Value("${upstox.oauth.redirect-uri:http://localhost:8080/trading-automation/callback}")
    private String upstoxRedirectUri;

    @Value("${trading.automation.sandbox.enabled:false}")
    private boolean sandbox;

    @Value("${trading.automation.base-url}")
    private String baseUrl;

}

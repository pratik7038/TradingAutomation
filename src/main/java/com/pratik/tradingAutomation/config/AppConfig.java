package com.pratik.tradingAutomation.config;

import com.upstox.ApiClient;
import com.upstox.auth.OAuth;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public ApiClient apiClient() {
        boolean sandbox = applicationProperties.isSandbox();
        ApiClient apiClient = new ApiClient(sandbox);
        OAuth OAUTH2 = (OAuth) apiClient.getAuthentication("OAUTH2");
        OAUTH2.setAccessToken(applicationProperties.getAccessToken());
        com.upstox.Configuration.setDefaultApiClient(apiClient);
        return apiClient;
    }

    @Bean
    public OpenAPI tradingAutomationOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Trading Automation API")
                        .version("v1"));
    }

}

package com.pratik.tradingAutomation.controller;

import com.pratik.tradingAutomation.config.ApplicationProperties;
import com.upstox.ApiClient;
import com.upstox.ApiException;
import com.upstox.api.PlaceOrderV3Request;
import com.upstox.api.PlaceOrderV3Response;
import io.swagger.client.api.OrderApiV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderApi {

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ApiClient apiClient;
    @Autowired
    private ApplicationProperties applicationProperties;

    @DeleteMapping("/order/cancel")
    public String cancelOrder(@RequestParam("orderId") String orderId) {
        String url = applicationProperties.getBaseUrl() + "/v3/order/cancel?order_id=" + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + applicationProperties.getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
        return response.getBody();
    }


    @PostMapping("/order/place")
    public PlaceOrderV3Response placeOrder(PlaceOrderV3Request body) {
        OrderApiV3 orderApiV3 = new OrderApiV3();
        orderApiV3.setApiClient(apiClient);
        body.setSlice(false);
        try {
            PlaceOrderV3Response result = orderApiV3.placeOrder(body);
            return result;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}

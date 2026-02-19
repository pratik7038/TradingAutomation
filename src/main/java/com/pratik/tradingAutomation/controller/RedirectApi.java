package com.pratik.tradingAutomation.controller;

import com.pratik.tradingAutomation.upstox.AuthorizationCodeStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectApi {

    private final AuthorizationCodeStore authorizationCodeStore;

    public RedirectApi(AuthorizationCodeStore authorizationCodeStore) {
        this.authorizationCodeStore = authorizationCodeStore;
    }

    @GetMapping({"/callback", "/login/upstox-v2"})
    public String handleCallback(@RequestParam String code,
                                 @RequestParam(required = false) String state) {
        if (state != null && !state.isBlank()) {
            authorizationCodeStore.validateStateOrThrow(state);
        }
        authorizationCodeStore.setLatestCode(code);
        System.out.println("Authorization Code = " + code);
        return "Code received";
    }

}

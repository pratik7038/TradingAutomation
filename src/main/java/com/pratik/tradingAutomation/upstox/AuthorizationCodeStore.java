package com.pratik.tradingAutomation.upstox;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AuthorizationCodeStore {

    private final AtomicReference<String> latestCode = new AtomicReference<>();
    private final AtomicReference<String> latestState = new AtomicReference<>();

    public void setLatestCode(String code) {
        latestCode.set(code);
    }

    public void setLatestState(String state) {
        latestState.set(state);
    }

    public Optional<String> getLatestCode() {
        return Optional.ofNullable(latestCode.get());
    }

    public Optional<String> getLatestState() {
        return Optional.ofNullable(latestState.get());
    }

    public void validateStateOrThrow(String returnedState) {
        String expected = latestState.get();
        if (expected == null || expected.isBlank()) {
            throw new IllegalStateException("Missing stored OAuth state");
        }
        if (returnedState == null || returnedState.isBlank()) {
            throw new IllegalStateException("Missing returned OAuth state");
        }
        if (!expected.equals(returnedState)) {
            throw new IllegalStateException("Invalid OAuth state");
        }
    }
}

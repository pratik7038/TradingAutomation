package com.pratik.tradingAutomation.controller;

import com.pratik.tradingAutomation.upstox.UpstoxClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trading")
public class TradingController {

    private final UpstoxClient upstoxClient;

    public TradingController(UpstoxClient upstoxClient) {
        this.upstoxClient = upstoxClient;
    }

    @GetMapping("/quotes/ltp")
    @Operation(summary = "Fetch LTP for an instrument. Example: NSE_EQ|INE848E01016")
    public String getLtp(@RequestParam("instrumentKey") String instrumentKey) {
        return upstoxClient.getLtp(instrumentKey);
    }
}

package com.spribe.test.controller;

import com.spribe.test.dto.CurrencyRateDTO;
import com.spribe.test.exception.CurrencyRateTaskException;
import com.spribe.test.service.CurrencyRateService;
import com.spribe.test.util.EnvironmentUtil;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencyRates") // Базовий шлях для контролера
@Log4j2
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;
    private final EnvironmentUtil environmentUtil;

    public CurrencyRateController(CurrencyRateService currencyRateService, EnvironmentUtil environmentUtil) {
        this.currencyRateService = currencyRateService;
        this.environmentUtil = environmentUtil;
    }

    @PostMapping(value = "/update", produces = "application/json")
    public ResponseEntity<String> updateCurrencyRates() {
        try {
            String ratesUrl = environmentUtil.getCurrencyRates().concat(environmentUtil.getAppId());
            currencyRateService.getAndSaveCurrencyRates(ratesUrl);
            return ResponseEntity.ok("Currency rates have been updated successfully.");
        } catch (CurrencyRateTaskException e) {
            log.error("Error updating currency rates: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Failed to update currency rates: " + e.getMessage());
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CurrencyRateDTO>> getCurrencyRates() {
        List<CurrencyRateDTO> lastCurrencyRateDTOs = currencyRateService.getLastCurrencyRateDTOs();
        return ResponseEntity.ok(lastCurrencyRateDTOs);
    }
}

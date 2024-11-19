package com.spribe.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spribe.test.dto.CurrencyDTO;
import com.spribe.test.exception.CurrencyRateTaskException;
import com.spribe.test.service.CurrencyService;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
@Log4j2
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping(value = "/update", produces = "application/json")
    public ResponseEntity<String> updateCurrencies() {
        try {
            currencyService.getAndSaveNewCurrencies();
            return ResponseEntity.ok("Currencies have been updated successfully.");
        } catch (JsonProcessingException e) {
            log.error("Error updating currencies: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to update currencies: " + e.getMessage());
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CurrencyDTO>> getCurrencies() {
        List<CurrencyDTO> currencies = currencyService.getCurrencyDTOs();
        return ResponseEntity.ok(currencies);
    }

    @PostMapping(value = "/{currencyCode}", produces = "application/json")
    public ResponseEntity<String> addCurrencyForUpdating(@PathVariable String currencyCode) {
        try {
            currencyService.addCurrencyToUpdating(currencyCode);
            return ResponseEntity.ok("Currency has been added for updating.");
        } catch (CurrencyRateTaskException e) {
            log.error("Error adding currency {}: {}", currencyCode, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to add currency: " + e.getMessage());
        }
    }
}

package com.spribe.test.model;

import com.spribe.test.dto.CurrencyRateDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CurrencyRateTest {


    @Test
    void testToDTO() {
        // Given
        Currency fromCurrency = new Currency();
        fromCurrency.setCode("USD");
        fromCurrency.setName("US Dollar");

        Currency toCurrency = new Currency();
        toCurrency.setCode("EUR");
        toCurrency.setName("Euro");

        CurrencyRatePack currencyRatePack = new CurrencyRatePack();
        currencyRatePack.setStartDate(new Date());
        currencyRatePack.setUnloadDate(new Date());
        currencyRatePack.setOuterSystem(
                OuterSystem.builder()
                        .code("QRW")
                        .build());

        CurrencyRate currencyRate = CurrencyRate.builder()
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .rate(new BigDecimal("1.2345"))
                .currencyRatePack(currencyRatePack)
                .build();

        // When
        CurrencyRateDTO currencyRateDTO = currencyRate.toDTO();

        // Then
        assertNotNull(currencyRateDTO);
        assertEquals(fromCurrency.getCode(), currencyRateDTO.getFromCurrency());
        assertEquals(toCurrency.getCode(), currencyRateDTO.getToCurrency());
        assertEquals(currencyRate.getRate(), currencyRateDTO.getRate());
        assertEquals(currencyRate.getCurrencyRatePack().getOuterSystem().getCode(), currencyRateDTO.getOuterSystemCode());
        assertEquals(currencyRate.getCurrencyRatePack().getStartDate(), currencyRateDTO.getStartDate());
        assertEquals(currencyRate.getCurrencyRatePack().getUnloadDate(), currencyRateDTO.getUnloadDate());
    }
}
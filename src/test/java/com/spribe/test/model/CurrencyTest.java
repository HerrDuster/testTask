package com.spribe.test.model;

import com.spribe.test.dto.CurrencyDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void testToDTO() {
        // Given
        Currency currency = new Currency();
        currency.setCode("USD");
        currency.setName("US Dollar");

        // When
        CurrencyDTO currencyDTO = currency.toDTO();

        // Then
        assertNotNull(currencyDTO);
        assertEquals(currency.getCode(), currencyDTO.getCode());
        assertEquals(currency.getName(), currencyDTO.getName());
    }
}
package com.spribe.test.service;

import com.spribe.test.dao.CurrencyRepository;
import com.spribe.test.dto.CurrencyDTO;
import com.spribe.test.model.Currency;
import com.spribe.test.service.ServiceImpl.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetCurrencyDTOs() {
        // Given
        List<Currency> currencies = List.of(
                new Currency(1L, "USD", "US Dollar", true),
                new Currency(2L, "EUR", "Euro", true)
        );
        when(currencyRepository.findAll()).thenReturn(currencies);

        // When
        List<CurrencyDTO> currencyDTOs = currencyService.getCurrencyDTOs();

        // Then
        assertEquals(2, currencyDTOs.size());
        assertEquals("USD", currencyDTOs.get(0).getCode());
        assertEquals("US Dollar", currencyDTOs.get(0).getName());
    }

    @Test
    void testAddCurrencyToUpdating() throws Exception {
        // Given
        String currencyCode = "USD";
        Currency currency = new Currency(1L, "USD", "US Dollar", false);
        when(currencyRepository.getCurrencyByCode(currencyCode)).thenReturn(currency);

        // When
        currencyService.addCurrencyToUpdating(currencyCode);

        // Then
        assertTrue(currency.getUsed());
        verify(currencyRepository, times(1)).save(currency);
    }
    @Test
    void testAddCurrencyToUpdatingWhenCurrencyDoesNotExist() {
        // Given
        String currencyCode = "USDT";
        when(currencyRepository.getCurrencyByCode(currencyCode)).thenReturn(null);

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            currencyService.addCurrencyToUpdating(currencyCode);
        });

        assertEquals("Currency is not exist", exception.getMessage());
        verify(currencyRepository, never()).save(any());
    }

    @Test
    void testGetCurrenciesForUpdating() {
        // Given
        List<Currency> currencies = List.of(
                new Currency(1L, "USD", "US Dollar", true),
                new Currency(2L, "EUR", "Euro", true),
                new Currency(3L, "GBP", "British Pound", true)
        );
        when(currencyRepository.getByUsedTrue()).thenReturn(currencies);
        // When
        String result = currencyService.getCurrenciesForUpdating();
        // Then
        assertEquals("USD,EUR,GBP", result);
    }
}
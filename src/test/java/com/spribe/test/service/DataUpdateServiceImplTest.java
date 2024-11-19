package com.spribe.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spribe.test.exception.CurrencyRateTaskException;
import com.spribe.test.service.ServiceImpl.DataUpdateServiceImpl;
import com.spribe.test.util.EnvironmentUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

class DataUpdateServiceImplTest {

    @Mock
    private CurrencyRateService currencyRateService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private EnvironmentUtil environmentUtil;

    @Mock
    private ScheduledExecutorService executor;

    @InjectMocks
    private DataUpdateServiceImpl dataUpdateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(dataUpdateService, "executor", executor);
        when(environmentUtil.getCurrencyRates()).thenReturn("mockCurrencyRates");
        when(environmentUtil.getAppId()).thenReturn("mockAppId");
    }

    @Test
    void updateData_shouldInvokeDependencies() throws JsonProcessingException, CurrencyRateTaskException {
        // Arrange
        when(environmentUtil.getCurrencyRates()).thenReturn("currencyRates");
        when(environmentUtil.getAppId()).thenReturn("appId");

        // Act
        dataUpdateService.updateData();

        // Assert
        verify(currencyService).getAndSaveNewCurrencies();
        verify(currencyRateService).getAndSaveCurrencyRates("currencyRatesappId");
    }

    @Test
    void updateData_shouldHandleExceptions() throws JsonProcessingException, CurrencyRateTaskException {
        // Arrange
        doThrow(new CurrencyRateTaskException("Test exception"))
                .when(currencyRateService).getAndSaveCurrencyRates(anyString());

        // Act
        dataUpdateService.updateData();

        // Assert
        verify(currencyService).getAndSaveNewCurrencies();
        verify(currencyRateService).getAndSaveCurrencyRates(anyString());
    }

    @Test
    void runScheduler_shouldScheduleTask() {
        // Act
        dataUpdateService.runScheduler();

        // Assert
        verify(executor).scheduleAtFixedRate(any(Runnable.class), eq(0L), eq(1L), eq(TimeUnit.HOURS));
    }
}

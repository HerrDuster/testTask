package com.spribe.test.service;

import com.spribe.test.dao.CurrencyRatePackRepository;
import com.spribe.test.dao.CurrencyRateRepository;
import com.spribe.test.dao.CurrencyRepository;
import com.spribe.test.dao.OuterSystemRepository;
import com.spribe.test.dto.CurrencyRateResponse;
import com.spribe.test.exception.CurrencyRateTaskException;
import com.spribe.test.model.Currency;
import com.spribe.test.model.CurrencyRate;
import com.spribe.test.model.CurrencyRatePack;
import com.spribe.test.model.OuterSystem;
import com.spribe.test.model.enums.COuterSystem;
import com.spribe.test.service.ServiceImpl.CurrencyRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyRateServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private CurrencyRatePackRepository currencyRatePackRepository;
    @Mock
    private OuterSystemRepository outerSystemRepository;
    @Mock
    private CurrencyRateRepository currencyRateRepository;

    private CurrencyRateServiceImpl currencyRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyRateService = new CurrencyRateServiceImpl(
                currencyRepository,
                currencyRatePackRepository,
                outerSystemRepository,
                currencyRateRepository
        );
    }

    @Test
    void testGetCurrencyRateResponse_ValidResponse() throws CurrencyRateTaskException {
        // given
        String mockUrl = "http://mock-url/api";
        CurrencyRateResponse mockResponse = getMockCurrencyRateResponse();

        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForObject(mockUrl, CurrencyRateResponse.class)).thenReturn(mockResponse);

        // when
        CurrencyRateResponse response = currencyRateService.getCurrencyRateResponse(mockUrl);

        // then
        assertNotNull(response);
        assertEquals("USD", response.getBaseCurrencyCode());
        assertEquals(1, response.getToCurrencyRateMap().size());
    }

    @Test
    void testGetCurrencyRateResponse_NullResponse() {
        // given
        String mockUrl = "http://mock-url/api";

        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForObject(mockUrl, CurrencyRateResponse.class)).thenReturn(null);

        // when & then
        assertThrows(CurrencyRateTaskException.class, () -> currencyRateService.getCurrencyRateResponse(mockUrl));
    }

    @Test
    void testGetAllCurrencyRatesFromCurrencyRateResponse() {
        // given
        CurrencyRateResponse response = getMockCurrencyRateResponse();
        Currency baseCurrency = new Currency();
        baseCurrency.setCode("USD");

        when(currencyRepository.getCurrencyByCode("USD")).thenReturn(baseCurrency);
        when(currencyRepository.getCurrencyByCode("EUR")).thenReturn(new Currency(0L,"EUR", "Euro", false));

        // when
        List<CurrencyRate> rates = currencyRateService.getAllCurrencyRatesFromCurrencyRateResponse(response);

        // then
        assertNotNull(rates);
        assertEquals(1, rates.size());
        assertEquals("USD", rates.get(0).getFromCurrency().getCode());
        assertEquals("EUR", rates.get(0).getToCurrency().getCode());
    }

    @Test
    void testProcessAndSaveCurrencyRates() {
        // given
        CurrencyRatePack mockPack = new CurrencyRatePack();
        mockPack.setStartDate(new Date());

        when(currencyRatePackRepository.findFirstByOrderByStartDateDesc()).thenReturn(mockPack);

        List<CurrencyRate> mockRates = Collections.singletonList(new CurrencyRate());
        CurrencyRateResponse mockResponse = getMockCurrencyRateResponse();

        // when
        currencyRateService.processAndSaveCurrencyRates(mockRates, mockResponse);

        // then
        verify(currencyRatePackRepository, atLeastOnce()).findFirstByOrderByStartDateDesc();
    }

    @Test
    void testGetLastCurrencyRateDTOs_WithCache() {
        // given
        CurrencyRate mockRate = new CurrencyRate();
        mockRate.setRate(BigDecimal.valueOf(1.2));
        currencyRateService.setCacheCurrencyRate(Collections.singletonList(mockRate));

        // when
        var result = currencyRateService.getLastCurrencyRateDTOs();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetLastCurrencyRateDTOs_WithoutCache() {
        // given
        CurrencyRatePack mockPack = new CurrencyRatePack();
        mockPack.setStartDate(new Date());
        when(currencyRatePackRepository.findFirstByOrderByStartDateDesc()).thenReturn(mockPack);
        when(currencyRateRepository.getByCurrencyRatePack(mockPack)).thenReturn(Collections.emptyList());

        // when
        var result = currencyRateService.getLastCurrencyRateDTOs();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private CurrencyRateResponse getMockCurrencyRateResponse() {
        CurrencyRateResponse response = new CurrencyRateResponse();
        response.setBaseCurrencyCode("USD");
        Map<String, BigDecimal> rateMap = new HashMap<>();
        rateMap.put("EUR", BigDecimal.valueOf(0.85));
        response.setToCurrencyRateMap(rateMap);
        return response;
    }
}

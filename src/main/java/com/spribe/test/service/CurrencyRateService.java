package com.spribe.test.service;

import com.spribe.test.dto.CurrencyRateDTO;
import com.spribe.test.dto.CurrencyRateResponse;
import com.spribe.test.exception.CurrencyRateTaskException;
import com.spribe.test.model.CurrencyRate;

import java.util.List;


public interface CurrencyRateService {

   void getAndSaveCurrencyRates(String url) throws CurrencyRateTaskException;

   void saveCurrencyRatesWithPack(List<CurrencyRate> currencyRates, CurrencyRateResponse currencyRateResponse);

   void processAndSaveCurrencyRates(List<CurrencyRate> currencyRates, CurrencyRateResponse currencyRateResponse);

   List<CurrencyRate> getAllCurrencyRatesFromCurrencyRateResponse(CurrencyRateResponse currencyRateResponse);

   CurrencyRateResponse getCurrencyRateResponse(String url) throws CurrencyRateTaskException;

   List<CurrencyRateDTO> getLastCurrencyRateDTOs();

}

package com.spribe.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spribe.test.dto.CurrencyDTO;
import com.spribe.test.exception.CurrencyRateTaskException;


import java.util.List;

public interface CurrencyService {
    String getCurrencyStringResponse();

    void saveUpdatedCurrency(String currencyStringResponse) throws JsonProcessingException;

    void getAndSaveNewCurrencies() throws JsonProcessingException;

    List<CurrencyDTO> getCurrencyDTOs();

    void addCurrencyToUpdating(String currencyCode) throws CurrencyRateTaskException;

    String getCurrenciesForUpdating();
}

package com.spribe.test.service.ServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spribe.test.dao.CurrencyRepository;
import com.spribe.test.dto.CurrencyDTO;
import com.spribe.test.exception.CurrencyRateTaskException;
import com.spribe.test.model.Currency;
import com.spribe.test.service.CurrencyService;
import com.spribe.test.util.EnvironmentUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final EnvironmentUtil environmentUtil;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, EnvironmentUtil environmentUtil) {
        this.currencyRepository = currencyRepository;
        this.environmentUtil = environmentUtil;
    }

    @Transactional
    @Override
    public void getAndSaveNewCurrencies() throws JsonProcessingException {
        String currencyStringResponse = getCurrencyStringResponse();
        saveUpdatedCurrency(currencyStringResponse);
    }

    @Override
    public String getCurrencyStringResponse() {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(environmentUtil.getCurrencies(), String.class);
    }

    @Override
    public void saveUpdatedCurrency(String currencyStringResponse) throws JsonProcessingException {

        Map<String, String> curCodeCurNameMap = new ObjectMapper().readValue(currencyStringResponse, HashMap.class);

        for (Map.Entry<String, String> entry : curCodeCurNameMap.entrySet()) {
            String curCode = entry.getKey();
            String curName = entry.getValue();

            Currency currencyFromDb = currencyRepository.getCurrencyByCode(curCode);

            if (Objects.nonNull(currencyFromDb) && !Objects.equals(currencyFromDb.getName(), curName)) {
                currencyFromDb.setName(curName);
                currencyRepository.save(currencyFromDb);
            } else if (Objects.isNull(currencyFromDb)) {
                currencyRepository.save(new Currency(null, curCode, curName, false));
            }
        }

    }

    @Transactional
    @Override
    public List<CurrencyDTO> getCurrencyDTOs() {
        return currencyRepository.findAll().stream()
                .map(Currency::toDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addCurrencyToUpdating(String currencyCode) throws CurrencyRateTaskException {

        Currency currencyByCode = currencyRepository.getCurrencyByCode(currencyCode);
        if (Objects.nonNull(currencyByCode)) {
            currencyByCode.setUsed(true);
            currencyRepository.save(currencyByCode);
        } else {
            throw new CurrencyRateTaskException("Currency is not exist");
        }

    }

    @Override
    public String getCurrenciesForUpdating() {
        return currencyRepository.getByUsedTrue()
                .stream()
                .map(Currency::getCode)
                .collect(Collectors.joining(","));
    }


}

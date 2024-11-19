package com.spribe.test.service.ServiceImpl;

import com.spribe.test.dao.CurrencyRatePackRepository;
import com.spribe.test.dao.CurrencyRateRepository;
import com.spribe.test.dao.CurrencyRepository;
import com.spribe.test.dao.OuterSystemRepository;
import com.spribe.test.dto.CurrencyRateDTO;
import com.spribe.test.dto.CurrencyRateResponse;
import com.spribe.test.exception.CurrencyRateTaskException;
import com.spribe.test.model.Currency;
import com.spribe.test.model.CurrencyRate;
import com.spribe.test.model.CurrencyRatePack;
import com.spribe.test.model.OuterSystem;
import com.spribe.test.model.enums.COuterSystem;
import com.spribe.test.service.CurrencyRateService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CurrencyRateServiceImpl implements CurrencyRateService {

    @Setter
    List<CurrencyRate> cacheCurrencyRate = new ArrayList<>();

    private final CurrencyRepository currencyRepository;
    private final CurrencyRatePackRepository currencyRatePackRepository;
    private final OuterSystemRepository outerSystemRepository;
    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyRateServiceImpl(CurrencyRepository currencyRepository,
                                   CurrencyRatePackRepository currencyRatePackRepository,
                                   OuterSystemRepository outerSystemRepository,
                                   CurrencyRateRepository currencyRateRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyRatePackRepository = currencyRatePackRepository;
        this.outerSystemRepository = outerSystemRepository;
        this.currencyRateRepository = currencyRateRepository;
    }


    @Override
    @Transactional
    public void getAndSaveCurrencyRates(String url) throws CurrencyRateTaskException {
        CurrencyRateResponse currencyRateResponse = getCurrencyRateResponse(url);
        List<CurrencyRate> allCurrencyRatesFromCurrencyRateResponse = getAllCurrencyRatesFromCurrencyRateResponse(currencyRateResponse);
        cacheCurrencyRate = allCurrencyRatesFromCurrencyRateResponse;
        processAndSaveCurrencyRates(allCurrencyRatesFromCurrencyRateResponse, currencyRateResponse);
    }

    @Override
    public void processAndSaveCurrencyRates(List<CurrencyRate> currencyRates, CurrencyRateResponse currencyRateResponse) {
        CurrencyRatePack lastCurrencyRatePack = currencyRatePackRepository.findFirstByOrderByStartDateDesc();
        if (Objects.nonNull(lastCurrencyRatePack)) {
            if (!Objects.equals(lastCurrencyRatePack.getStartDate(), currencyRateResponse.getStartDate())) {
                saveCurrencyRatesWithPack(currencyRates, currencyRateResponse);
            }
        } else {
            saveCurrencyRatesWithPack(currencyRates, currencyRateResponse);
        }
    }

    @Override
    public void saveCurrencyRatesWithPack(List<CurrencyRate> currencyRates, CurrencyRateResponse currencyRateResponse) {
        OuterSystem outerSystem = outerSystemRepository.getByCode(COuterSystem.OXR.name());
        CurrencyRatePack currencyRatePack = currencyRatePackRepository.save(
                CurrencyRatePack.builder().
                        outerSystem(outerSystem)
                        .startDate(currencyRateResponse.getStartDate())
                        .unloadDate(new Date())
                        .build());

        for (CurrencyRate currencyRate : currencyRates) {
            currencyRate.setCurrencyRatePack(currencyRatePack);
            currencyRateRepository.save(currencyRate);
        }
    }

    @Override
    public List<CurrencyRate> getAllCurrencyRatesFromCurrencyRateResponse(CurrencyRateResponse currencyRateResponse) {

        if (Objects.nonNull(currencyRateResponse)) {
            List<CurrencyRate> currencyRates = new ArrayList<>();

            Currency baseCurrency = currencyRepository.getCurrencyByCode(currencyRateResponse.getBaseCurrencyCode());

            for (Map.Entry<String, BigDecimal> entry : currencyRateResponse.getToCurrencyRateMap().entrySet()) {
                String toCurrencyCode = entry.getKey();
                BigDecimal rate = entry.getValue();

                CurrencyRate currencyRate = CurrencyRate.builder()
                        .fromCurrency(baseCurrency)
                        .toCurrency(currencyRepository.getCurrencyByCode(toCurrencyCode))
                        .rate(rate)
                        .build();
                currencyRates.add(currencyRate);
            }

            return currencyRates;
        }
        return null;
    }

    @Override
    public CurrencyRateResponse getCurrencyRateResponse(String url) throws CurrencyRateTaskException {
        final RestTemplate restTemplate = new RestTemplate();
        CurrencyRateResponse response = restTemplate.getForObject(url, CurrencyRateResponse.class);
        if (Objects.isNull(response))
            throw new CurrencyRateTaskException("Sorry! Server is not available!");
        return response;
    }

    @Transactional
    @Override
    public List<CurrencyRateDTO> getLastCurrencyRateDTOs() {
        if (!Objects.isNull(cacheCurrencyRate) && !cacheCurrencyRate.isEmpty()) {
            return cacheCurrencyRate.stream()
                    .map(CurrencyRate::toDTO).collect(Collectors.toList());
        }
        final CurrencyRatePack currencyRatePack = currencyRatePackRepository.findFirstByOrderByStartDateDesc();
        List<CurrencyRate> currencyRates = currencyRateRepository.getByCurrencyRatePack(currencyRatePack);
       return currencyRates.stream()
                .map(CurrencyRate::toDTO).collect(Collectors.toList());
    }

}

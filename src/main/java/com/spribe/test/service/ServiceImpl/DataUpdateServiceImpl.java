package com.spribe.test.service.ServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spribe.test.exception.CurrencyRateTaskException;
import com.spribe.test.service.CurrencyRateService;
import com.spribe.test.service.CurrencyService;
import com.spribe.test.service.DataUpdateService;
import com.spribe.test.util.EnvironmentUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class DataUpdateServiceImpl implements DataUpdateService {

    private final CurrencyRateService currencyRateService;
    private final CurrencyService currencyService;
    private final EnvironmentUtil environmentUtil;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Autowired
    public DataUpdateServiceImpl(CurrencyRateService currencyRateService, CurrencyService currencyService, EnvironmentUtil environmentUtil) {
        this.currencyRateService = currencyRateService;
        this.currencyService = currencyService;
        this.environmentUtil = environmentUtil;
    }

    @Transactional
    public void updateData() {
        try {
            currencyService.getAndSaveNewCurrencies();
            currencyRateService.getAndSaveCurrencyRates(
                    environmentUtil.getCurrencyRates().concat(environmentUtil.getAppId())
            );

        } catch (JsonProcessingException | CurrencyRateTaskException e) {
            log.error(e.getMessage());
        }
        log.info("Currency rates are updated");
    }

    @PostConstruct
    public void runScheduler() {
        log.info("Starting scheduler to update currency rates every hour.");
        executor.scheduleAtFixedRate(this::updateData, 0, 1, TimeUnit.HOURS);
    }

}

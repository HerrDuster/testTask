package com.spribe.test.configuration;

import com.spribe.test.service.CurrencyRateService;
import com.spribe.test.service.CurrencyService;
import com.spribe.test.service.DataUpdateService;
import com.spribe.test.service.ServiceImpl.DataUpdateServiceImpl;
import com.spribe.test.util.EnvironmentUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class CurrencyUpdateConfig {

    private final CurrencyRateService currencyRateService;
    private final CurrencyService currencyService;
    private final EnvironmentUtil environmentUtil;

    public CurrencyUpdateConfig(CurrencyRateService currencyRateService, CurrencyService currencyService, EnvironmentUtil environmentUtil) {
        this.currencyRateService = currencyRateService;
        this.currencyService = currencyService;
        this.environmentUtil = environmentUtil;
    }

    @Bean
    public DataUpdateService dataUpdateServiceRun() {
        DataUpdateService dataUpdateService = new DataUpdateServiceImpl(currencyRateService, currencyService, environmentUtil);
        dataUpdateService.runScheduler();
        return dataUpdateService;
    }
}

package com.spribe.test.util;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EnvironmentUtil {

    private static final String APP_ID = "APP_ID";
    private static final String CURRENCIES="CURRENCIES";
    private static final String CURRENCY_RATES="CURRENCY_RATES";

    private final Environment env;

    @Getter
    private String appId;

    @Getter
    private String currencies;

    @Getter
    private String currencyRates;
    @PostConstruct
    public void init() {
        appId = env.getProperty(APP_ID);
        currencies = env.getProperty(CURRENCIES);
        currencyRates = env.getProperty(CURRENCY_RATES);
    }

}
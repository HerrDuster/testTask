package com.spribe.test.dao;

import com.spribe.test.model.CurrencyRate;
import com.spribe.test.model.CurrencyRatePack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    List<CurrencyRate> getByCurrencyRatePack(CurrencyRatePack currencyRatePack);
}

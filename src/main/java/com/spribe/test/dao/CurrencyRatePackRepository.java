package com.spribe.test.dao;

import com.spribe.test.model.CurrencyRatePack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRatePackRepository extends JpaRepository<CurrencyRatePack, Long> {

    CurrencyRatePack findFirstByOrderByStartDateDesc();

}

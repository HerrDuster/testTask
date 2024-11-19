package com.spribe.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRateDTO implements Serializable {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private String outerSystemCode;
    private Date unloadDate;
    private Date startDate;

}

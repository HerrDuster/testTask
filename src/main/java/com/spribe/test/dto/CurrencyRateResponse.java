package com.spribe.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRateResponse implements Serializable {
        private String disclaimer;
        private String license;
        @JsonProperty("timestamp")
        @JsonDeserialize(using = TimestampToDateDeserializer.class)
        private Date startDate;
        @JsonProperty("base")
        private String baseCurrencyCode;
        @JsonProperty("rates")
        private Map<String, BigDecimal> toCurrencyRateMap;
}

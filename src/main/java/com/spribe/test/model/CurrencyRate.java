package com.spribe.test.model;

import com.spribe.test.dto.CurrencyRateDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Builder
@Table(name = "cur_rate")
public class CurrencyRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_cur_id", referencedColumnName = "id")
    private Currency fromCurrency;
    @ManyToOne
    @JoinColumn(name = "to_cur_id", referencedColumnName = "id")
    private Currency toCurrency;
    @Column(name = "rate")
    private BigDecimal rate;
    @ManyToOne
    @JoinColumn(name = "cur_rate_pack_id", referencedColumnName = "id")
    private CurrencyRatePack currencyRatePack;

    public CurrencyRateDTO toDTO() {
        return CurrencyRateDTO.builder()
                .fromCurrency(this.fromCurrency.getCode())
                .toCurrency(this.toCurrency.getCode())
                .rate(this.rate)
                .outerSystemCode(Optional.ofNullable(this.getCurrencyRatePack())
                        .map(CurrencyRatePack::getOuterSystem).map(OuterSystem::getCode).orElse(null))
                .startDate(Optional.ofNullable(this.getCurrencyRatePack()).map(CurrencyRatePack::getStartDate).orElse(null))
                .unloadDate(Optional.ofNullable(this.getCurrencyRatePack()).map(CurrencyRatePack::getUnloadDate).orElse(null))
                .build();
    }
}

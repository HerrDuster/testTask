package com.spribe.test.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Builder
@Table(name = "cur_rate_pack")
public class CurrencyRatePack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "outer_system_id", referencedColumnName = "id")
    private OuterSystem outerSystem;
    @Column(name ="unload_time")
    private Date unloadDate;
    @Column(name ="start_time")
    private Date startDate;
}

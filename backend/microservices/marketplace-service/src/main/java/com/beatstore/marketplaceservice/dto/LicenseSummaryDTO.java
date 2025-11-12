package com.beatstore.marketplaceservice.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LicenseSummaryDTO {
    private String name;
    private BigDecimal defaultPrice;
}

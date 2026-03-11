package com.beatstore.marketplaceservice.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LicenseTemplateSummaryDTO {
    private String name;
    private BigDecimal defaultPrice;

    public LicenseTemplateSummaryDTO(String name, BigDecimal defaultPrice) {
        this.name = name;
        this.defaultPrice = defaultPrice;
    }
}

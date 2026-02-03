package com.beatstore.marketplacerestclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Getter
public class AssignLicenseCommand {
    private String contentHash;
    private Map<String, BigDecimal> licenseHashToCustomPrice;
}

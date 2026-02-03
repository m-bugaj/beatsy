package com.beatstore.marketplaceservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@ToString
public class AssignLicenseCommand {
    private String contentHash;
    private Map<String, BigDecimal> licenseHashToCustomPrice;

    @JsonIgnore
    @Setter
    private String userHash;

}

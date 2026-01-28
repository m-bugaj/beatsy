package com.beatstore.marketplaceservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class LicenseCommand {
    private String name;
    private BigDecimal defaultPrice;

    @JsonIgnore
    @Setter
    private String userHash;

    private Integer streamLimit;
    private Integer physicalSalesLimit;
    private Boolean allowMp3Download;
    private Boolean allowWavDownload;
    private Boolean allowStemsDownload;
    private Boolean allowLivePerformance;
    private Boolean allowBroadcast;
    private Boolean allowYtMonetization;
    private Boolean useInPaidAds;
    private Boolean useInVideoProjects;
    private Integer licenseDurationMonths;

    private Boolean applyToAllBeats = false;
}

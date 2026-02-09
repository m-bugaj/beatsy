package com.beatstore.marketplaceservice.model;

import com.beatstore.marketplaceservice.dto.LicenseCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "license_limit_config")
@Getter
@Setter
@NoArgsConstructor
public class LicenseLimitConfig {
    @Id
    @SequenceGenerator(
            name = "license_limit_config_id_seq",
            sequenceName = "license_limit_config_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "license_limit_config_id_seq"
    )
    private Long id;

    private Integer streamLimit;
    private Integer physicalSalesLimit;

    @Column(
            name = "allow_mp3_download",
            nullable = false
    )
    private Boolean allowMp3Download;

    private Boolean allowWavDownload;
    private Boolean allowStemsDownload;
    private Boolean allowLivePerformance;
    private Boolean allowBroadcast;
    private Boolean allowYtMonetization;
    private Boolean useInPaidAds;
    private Boolean useInVideoProjects;
    private Integer licenseDurationMonths;

    @OneToOne(mappedBy = "licenseLimitConfig")
    private License license;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public LicenseLimitConfig(LicenseCommand licenseCommand) {
        this.streamLimit = licenseCommand.getStreamLimit();
        this.physicalSalesLimit = licenseCommand.getPhysicalSalesLimit();
        this.allowMp3Download = licenseCommand.getAllowMp3Download();
        this.allowWavDownload = licenseCommand.getAllowWavDownload();
        this.allowStemsDownload = licenseCommand.getAllowStemsDownload();
        this.allowLivePerformance = licenseCommand.getAllowLivePerformance();
        this.allowBroadcast = licenseCommand.getAllowBroadcast();
        this.allowYtMonetization = licenseCommand.getAllowYtMonetization();
        this.useInPaidAds = licenseCommand.getUseInPaidAds();
        this.useInVideoProjects = licenseCommand.getUseInVideoProjects();
        this.licenseDurationMonths = licenseCommand.getLicenseDurationMonths();
    }
}

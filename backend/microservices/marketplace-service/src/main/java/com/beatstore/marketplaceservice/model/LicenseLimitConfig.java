package com.beatstore.marketplaceservice.model;

import com.beatstore.marketplaceservice.dto.LicenseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "license_limit_config")
@Getter
@Setter
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

    public LicenseLimitConfig(LicenseDTO licenseDTO) {
        this.streamLimit = licenseDTO.getStreamLimit();
        this.physicalSalesLimit = licenseDTO.getPhysicalSalesLimit();
        this.allowMp3Download = licenseDTO.getAllowMp3Download();
        this.allowWavDownload = licenseDTO.getAllowWavDownload();
        this.allowStemsDownload = licenseDTO.getAllowStemsDownload();
        this.allowLivePerformance = licenseDTO.getAllowLivePerformance();
        this.allowBroadcast = licenseDTO.getAllowBroadcast();
        this.allowYtMonetization = licenseDTO.getAllowYtMonetization();
        this.useInPaidAds = licenseDTO.getUseInPaidAds();
        this.useInVideoProjects = licenseDTO.getUseInVideoProjects();
        this.licenseDurationMonths = licenseDTO.getLicenseDurationMonths();
    }
}

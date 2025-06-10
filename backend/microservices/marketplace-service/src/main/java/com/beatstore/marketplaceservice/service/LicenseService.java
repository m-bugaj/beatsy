package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.dto.LicenseDTO;
import com.beatstore.marketplaceservice.model.Beat;
import com.beatstore.marketplaceservice.model.BeatLicense;
import com.beatstore.marketplaceservice.model.License;
import com.beatstore.marketplaceservice.model.LicenseLimitConfig;
import com.beatstore.marketplaceservice.repository.BeatLicenseRepository;
import com.beatstore.marketplaceservice.repository.BeatRepository;
import com.beatstore.marketplaceservice.repository.LicenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;
    private final BeatLicenseRepository beatLicenseRepository;
    private final BeatRepository beatRepository;

    public LicenseService(LicenseRepository licenseRepository, BeatLicenseRepository beatLicenseRepository, BeatRepository beatRepository) {
        this.licenseRepository = licenseRepository;
        this.beatLicenseRepository = beatLicenseRepository;
        this.beatRepository = beatRepository;
    }

    public void createLicense(LicenseDTO licenseDTO) {
        log.info("Creating license for userHash: {}", licenseDTO.getUserHash());

        LicenseLimitConfig licenseLimitConfig = new LicenseLimitConfig(licenseDTO);
        License license = License.builder()
                .name(licenseDTO.getName())
                .defaultPrice(licenseDTO.getDefaultPrice())
                .userHash(licenseDTO.getUserHash())
                .licenseLimitConfig(licenseLimitConfig)
                .hash(UUID.randomUUID().toString())
                .build();
        licenseRepository.save(license);

        log.info("License saved with hash: {}", license.getHash());

        applyLicenseToAllBeatsIfRequested(licenseDTO.getUserHash(), licenseDTO.getApplyToAllBeats(), license);
    }

    private void applyLicenseToAllBeatsIfRequested(String userHash, Boolean applyToAllBeats, License license) {
        if (Objects.nonNull(applyToAllBeats) && applyToAllBeats) {
            log.info("applyToAllBeats=true, assigning license to all beats for userHash: {}", userHash);
            Set<Beat> beats = beatRepository.findAllByUserHash(userHash);
            Set<BeatLicense> beatLicenses = beats.stream()
                    .map(beat -> new BeatLicense(beat, license, null))
                    .collect(Collectors.toSet());
            beatLicenseRepository.saveAll(beatLicenses);
            log.info("Assigned license to {} beats", beatLicenses.size());
        }
    }
}

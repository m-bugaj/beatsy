package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.dto.LicenseCommand;
import com.beatstore.marketplaceservice.model.Beat;
import com.beatstore.marketplaceservice.model.BeatLicense;
import com.beatstore.marketplaceservice.model.License;
import com.beatstore.marketplaceservice.model.LicenseLimitConfig;
import com.beatstore.marketplaceservice.repository.BeatLicenseRepository;
import com.beatstore.marketplaceservice.repository.BeatRepository;
import com.beatstore.marketplaceservice.repository.LicenseRepository;
import jakarta.transaction.Transactional;
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
    //TODO: W PZYSZŁOŚCI DODAĆ OBSŁUGĘ LIMITÓW (NP MAX 4 LICENCJE NA UŻYTKOWNIKA I DODANIE 5 MA RZUCIĆ BŁĘDEM)
    @Transactional
    public void createLicense(LicenseCommand licenseCommand) {
        log.info("Creating license for userHash: {}", licenseCommand.getUserHash());
        LicenseLimitConfig licenseLimitConfig = new LicenseLimitConfig(licenseCommand);
        License license = License.builder()
                .name(licenseCommand.getName())
                .defaultPrice(licenseCommand.getDefaultPrice())
                .userHash(licenseCommand.getUserHash())
                .licenseLimitConfig(licenseLimitConfig)
                .hash(UUID.randomUUID().toString())
                .build();
        licenseRepository.save(license);
        log.info("License saved with hash: {}", license.getHash());
        applyLicenseToAllBeatsIfRequested(licenseCommand.getUserHash(), licenseCommand.getApplyToAllBeats(), license);
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

package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.dto.LicenseDTO;
import com.beatstore.marketplaceservice.model.License;
import com.beatstore.marketplaceservice.model.LicenseLimitConfig;
import com.beatstore.marketplaceservice.repository.LicenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;

    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    public void createLicense(LicenseDTO licenseDTO) {
        LicenseLimitConfig licenseLimitConfig = new LicenseLimitConfig(licenseDTO);
        License license = License.builder()
                .name(licenseDTO.getName())
                .defaultPrice(licenseDTO.getDefaultPrice())
                .userHash(licenseDTO.getUserHash())
                .licenseLimitConfig(licenseLimitConfig)
                .hash(UUID.randomUUID().toString())
                .build();
        licenseRepository.save(license);
    }
}

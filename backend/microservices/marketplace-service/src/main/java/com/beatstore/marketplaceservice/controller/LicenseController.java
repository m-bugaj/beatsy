package com.beatstore.marketplaceservice.controller;

import com.beatstore.marketplaceservice.dto.LicenseDTO;
import com.beatstore.marketplaceservice.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("secured/licenses")
public class LicenseController {
    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping
    public ResponseEntity<Void> createLicense(@RequestBody LicenseDTO licenseDTO) {
        //TODO: userHash z sesji wyciagac do dto
        licenseService.createLicense(licenseDTO);
        return ResponseEntity.ok().build();
    }
}

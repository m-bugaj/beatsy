package com.beatstore.marketplaceservice.controller;

import com.beatstore.marketplaceservice.context.RequestContext;
import com.beatstore.marketplaceservice.dto.LicenseDTO;
import com.beatstore.marketplaceservice.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/secured/licenses")
public class LicenseController {
    private final LicenseService licenseService;
    private final RequestContext requestContext;

    public LicenseController(LicenseService licenseService, RequestContext requestContext) {
        this.licenseService = licenseService;
        this.requestContext = requestContext;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<Void> createLicense(@RequestBody LicenseDTO licenseDTO) {
        licenseDTO.setUserHash(requestContext.getUserHash());
        log.info("Creating license {}", licenseDTO);
        licenseService.createLicense(licenseDTO);
        return ResponseEntity.ok().build();
    }
}

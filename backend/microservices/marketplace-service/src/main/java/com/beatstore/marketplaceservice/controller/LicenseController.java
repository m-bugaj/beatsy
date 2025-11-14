package com.beatstore.marketplaceservice.controller;

import com.beatstore.marketplaceservice.context.RequestContext;
import com.beatstore.marketplaceservice.dto.LicenseCommand;
import com.beatstore.marketplaceservice.dto.LicenseSummaryDTO;
import com.beatstore.marketplaceservice.service.LicenseService;
import com.beatstore.marketplaceservice.utils.CollectionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    public ResponseEntity<Void> createLicense(@RequestBody LicenseCommand licenseCommand) {
        licenseCommand.setUserHash(requestContext.getUserHash());
        log.info("Creating license {}", licenseCommand);
        licenseService.createLicense(licenseCommand);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<CollectionWrapper<LicenseSummaryDTO>> getLicensesForMe() {
        String userHash = requestContext.getUserHash();
        Set<LicenseSummaryDTO> licenseSummaries = licenseService.getLicenseSummaries(userHash);
        return ResponseEntity.ok(CollectionWrapper.of(licenseSummaries));
    }
}

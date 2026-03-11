package com.beatstore.marketplaceservice.controller;

import com.beatstore.marketplaceservice.context.RequestContext;
import com.beatstore.marketplaceservice.dto.LicenseCommand;
import com.beatstore.marketplaceservice.dto.LicenseTemplateSummaryDTO;
import com.beatstore.marketplaceservice.service.LicenseTemplateService;
import com.beatstore.marketplaceservice.utils.CollectionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/secured/license")
public class LicenseTemplateController {
    private final LicenseTemplateService licenseTemplateService;
    private final RequestContext requestContext;

    public LicenseTemplateController(LicenseTemplateService licenseTemplateService, RequestContext requestContext) {
        this.licenseTemplateService = licenseTemplateService;
        this.requestContext = requestContext;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<Void> createLicenseTemplate(@RequestBody LicenseCommand licenseCommand) {
        licenseCommand.setUserHash(requestContext.getUserHash());
        log.info("Creating license {}", licenseCommand);
        licenseTemplateService.createLicense(licenseCommand);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<CollectionWrapper<LicenseTemplateSummaryDTO>> getLicenseTemplatesForMe() {
        String userHash = requestContext.getUserHash();
        Set<LicenseTemplateSummaryDTO> licenseSummaries = licenseTemplateService.getLicenseSummaries(userHash);
        return ResponseEntity.ok(CollectionWrapper.of(licenseSummaries));
    }
}

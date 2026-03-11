package com.beatstore.marketplaceservice.controller.internal;

import com.beatstore.marketplaceservice.dto.AssignLicenseCommand;
import com.beatstore.marketplaceservice.service.LicenseTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/internal/license")
public class LicenseTemplateControllerInternal {
    private final LicenseTemplateService licenseTemplateService;

    public LicenseTemplateControllerInternal(LicenseTemplateService licenseTemplateService) {
        this.licenseTemplateService = licenseTemplateService;
    }

    @PostMapping("/assign/user/{userHash}")
    public ResponseEntity<Void> assignLicenseTemplateToContent(
            @PathVariable String userHash,
            @RequestBody AssignLicenseCommand command) {
        command.setUserHash(userHash);
        log.info("Assign license to content {}", command);
        licenseTemplateService.assignLicensesToContent(command);
        return ResponseEntity.ok().build();
    }
}

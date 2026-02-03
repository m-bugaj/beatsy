package com.beatstore.marketplaceservice.controller.internal;

import com.beatstore.marketplaceservice.dto.AssignLicenseCommand;
import com.beatstore.marketplaceservice.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/internal/license")
public class LicenseControllerInternal {
    private final LicenseService licenseService;

    public LicenseControllerInternal(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/assign/user/{userHash}")
    public ResponseEntity<Void> assignLicenseToContent(
            @PathVariable String userHash,
            @RequestBody AssignLicenseCommand command) {
        command.setUserHash(userHash);
        log.info("Assign license to content {}", command);
        licenseService.assignLicensesToContent(command);
        return ResponseEntity.ok().build();
    }
}

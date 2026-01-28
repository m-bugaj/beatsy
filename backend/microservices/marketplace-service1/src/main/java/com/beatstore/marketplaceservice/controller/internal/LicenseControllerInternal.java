package com.beatstore.marketplaceservice.controller.internal;

import com.beatstore.marketplaceservice.context.RequestContext;
import com.beatstore.marketplaceservice.dto.AssignLicenseCommand;
import com.beatstore.marketplaceservice.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/internal/license")
public class LicenseControllerInternal {
    private final RequestContext requestContext;
    private final LicenseService licenseService;

    public LicenseControllerInternal(RequestContext requestContext, LicenseService licenseService) {
        this.requestContext = requestContext;
        this.licenseService = licenseService;
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignLicenseToContent(@RequestBody AssignLicenseCommand command) {
        command.setUserHash(requestContext.getUserHash());
        licenseService.assignLicensesToContent(command);
        return ResponseEntity.ok().build();
    }
}

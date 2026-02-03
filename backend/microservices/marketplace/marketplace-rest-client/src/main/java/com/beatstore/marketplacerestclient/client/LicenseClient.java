package com.beatstore.marketplacerestclient.client;

import com.beatstore.marketplacerestclient.dto.AssignLicenseCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(
//        name = "marketplace-service",
//        contextId = "marketplaceLicenseClient"
//)
@FeignClient(
        name = "marketplace-service",
        url = "http://localhost:8083"
)
public interface LicenseClient {

    @PostMapping("/internal/license/assign/user/{userHash}")
    Void assignLicenseToContent(
            @PathVariable String userHash,
            @RequestBody AssignLicenseCommand command
    );
}

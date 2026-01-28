package com.beatstore.marketplaceservice.client;

import com.beatstore.marketplaceservice.common.enums.content.ContentType;
import com.beatstore.marketplaceservice.dto.ContentBaseDto;
import com.beatstore.marketplaceservice.utils.CollectionWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "content-service")
public interface ContentClient {

    @PostMapping("/internal/content/user/{userHash}")
    CollectionWrapper<ContentBaseDto> getContentForUserHash(
            @PathVariable String userHash,
            @RequestParam ContentType contentType
            );
}

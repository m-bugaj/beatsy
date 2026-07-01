package com.beatstore.contentrestclient.client;

import com.beatstore.contentrestclient.dto.ContentDetailsDto;
import com.beatstore.contentrestclient.dto.FetchContentDetailsCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

@FeignClient(
        name = "content-service",
        url = "http://localhost:8084"
)
public interface ContentClient {

    @PostMapping("/internal/content/details")
    Map<String, ContentDetailsDto> getContentDetails(@RequestBody FetchContentDetailsCommand command);
}

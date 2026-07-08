package com.beatstore.contentservice.controller.internal;

import com.beatstore.contentrestclient.common.enums.ContentType;
import com.beatstore.contentrestclient.dto.ContentDetailsDto;
import com.beatstore.contentrestclient.dto.ContentForUserResponse;
import com.beatstore.contentrestclient.dto.FetchContentDetailsCommand;
import com.beatstore.contentservice.dto.ContentBaseDto;
import com.beatstore.contentservice.service.BeatService;
import com.beatstore.contentservice.utils.CollectionWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/internal/content")
public class ContentControllerInternal {
    private final BeatService beatService;

    public ContentControllerInternal(BeatService beatService) {
        this.beatService = beatService;
    }

    @GetMapping(path = "/user/{userHash}")
    public Set<ContentForUserResponse> getContentForUser(
            @PathVariable String userHash,
            @RequestParam ContentType contentType
            ) {
        return beatService.getContentForUser(userHash, contentType);
    }

    @PostMapping("/details")
    Map<String, ContentDetailsDto> getContentDetails(@RequestBody FetchContentDetailsCommand command) {
        return beatService.getContentDetails(command);
    }
}

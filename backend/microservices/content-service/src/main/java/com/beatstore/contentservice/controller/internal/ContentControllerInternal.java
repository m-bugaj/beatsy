package com.beatstore.contentservice.controller.internal;

import com.beatstore.contentservice.common.enums.ContentType;
import com.beatstore.contentservice.dto.ContentBaseDto;
import com.beatstore.contentservice.service.BeatService;
import com.beatstore.contentservice.utils.CollectionWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/internal/content")
public class ContentControllerInternal {
    private final BeatService beatService;

    public ContentControllerInternal(BeatService beatService) {
        this.beatService = beatService;
    }

    @GetMapping(path = "/user/{userHash}")
    public ResponseEntity<CollectionWrapper<ContentBaseDto>> getContentForUser(
            @PathVariable String userHash,
            @RequestParam ContentType contentType
            ) {
        Set<ContentBaseDto> content = beatService.getContentForUser(userHash, contentType);
        return ResponseEntity.ok(new CollectionWrapper<>(content));
    }
}

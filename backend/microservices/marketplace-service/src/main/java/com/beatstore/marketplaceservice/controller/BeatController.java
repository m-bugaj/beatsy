package com.beatstore.marketplaceservice.controller;

import com.beatstore.marketplaceservice.common.enums.FeedType;
import com.beatstore.marketplaceservice.context.RequestContext;
import com.beatstore.marketplaceservice.dto.BeatDetailsDTO;
import com.beatstore.marketplaceservice.dto.BeatSummaryDTO;
import com.beatstore.marketplaceservice.dto.BeatUploadCommand;
import com.beatstore.marketplaceservice.service.BeatService;
import com.beatstore.marketplaceservice.utils.CollectionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/secured/beat")
public class BeatController {
    private final BeatService beatService;
    private final RequestContext requestContext;

    public BeatController(BeatService beatService, RequestContext requestContext) {
        this.beatService = beatService;
        this.requestContext = requestContext;
    }

    @GetMapping("/{beatHash}")
    public ResponseEntity<BeatDetailsDTO> getBeatDetails(@PathVariable String beatHash) {
        String userHash = requestContext.getUserHash();
        BeatDetailsDTO beatDetails = beatService.getBeatDetails(beatHash, userHash);
        return ResponseEntity.ok(beatDetails);
    }

    @GetMapping("/paged")
    public ResponseEntity<CollectionWrapper<BeatSummaryDTO>> getAllBeatsPaged(
            @RequestParam(defaultValue = "DISCOVER") FeedType feedType,
            @PageableDefault(size = 40) Pageable pageable
    ) {
        Set<BeatSummaryDTO> beatSummaries = beatService.getBeatSummaries(feedType, pageable);
        return ResponseEntity.ok(CollectionWrapper.of(beatSummaries));
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadNewBeat(
            @RequestPart("beatData") BeatUploadCommand beatUploadCommand,
            @RequestPart(value = "mp3File", required = false) MultipartFile mp3File,
            @RequestPart(value = "untaggedWavFile", required = true) MultipartFile untaggedWavFile,
            @RequestPart(value = "stemsFile", required = false) MultipartFile stemsFile
    ) {
        beatUploadCommand.setUserHash(requestContext.getUserHash());
        beatService.uploadNewBeat(beatUploadCommand, mp3File, untaggedWavFile, stemsFile);
        return ResponseEntity.ok().build();
    }
}

package com.beatstore.marketplaceservice.controller;

import com.beatstore.marketplaceservice.context.RequestContext;
import com.beatstore.marketplaceservice.dto.BeatUploadCommand;
import com.beatstore.marketplaceservice.service.BeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

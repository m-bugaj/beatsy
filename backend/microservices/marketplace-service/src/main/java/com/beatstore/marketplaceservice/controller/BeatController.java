package com.beatstore.marketplaceservice.controller;

import com.beatstore.marketplaceservice.dto.BeatUploadDTO;
import com.beatstore.marketplaceservice.model.Beat;
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

    public BeatController(BeatService beatService) {
        this.beatService = beatService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadNewBeat(
            @RequestPart("beatData") BeatUploadDTO beatUploadDTO,
            @RequestPart(value = "mp3File", required = false) MultipartFile mp3File,
            @RequestPart(value = "untaggedWavFile", required = true) MultipartFile untaggedWavFile,
            @RequestPart(value = "stemsFile", required = false) MultipartFile stemsFile
    ) {
        //zrobic sesje i wyciagnac z niej userHash
//        beatUploadDTO.getUserHash();

        beatService.uploadNewBeat(beatUploadDTO, mp3File, untaggedWavFile, stemsFile);
        return ResponseEntity.ok().build();
    }
}

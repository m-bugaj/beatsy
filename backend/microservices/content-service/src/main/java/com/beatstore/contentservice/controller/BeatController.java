package com.beatstore.contentservice.controller;

import com.beatstore.contentservice.context.RequestContext;
import com.beatstore.contentservice.dto.BeatDetailsDto;
import com.beatstore.contentservice.dto.BeatRequest;
import com.beatstore.contentservice.service.BeatService;
import com.beatstore.contentservice.utils.CollectionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
            @RequestBody BeatRequest beatRequest
//            @RequestPart("beatData") BeatRequest beatRequest
//            @RequestPart(value = "mp3File", required = false) MultipartFile mp3File,
//            @RequestPart(value = "untaggedWavFile", required = true) MultipartFile untaggedWavFile,
//            @RequestPart(value = "stemsFile", required = false) MultipartFile stemsFile
    ) {
        beatRequest.setUserHash(requestContext.getUserHash());
        beatService.uploadNewBeat(beatRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{beatHash}")
    public ResponseEntity<Void> updateBeat(
            @PathVariable String beatHash,
            @RequestBody BeatRequest beatRequest
    ) {
        beatRequest.setUserHash(requestContext.getUserHash());
        beatService.updateBeat(beatHash, beatRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{beatHash}")
    public ResponseEntity<BeatDetailsDto> getBeatDetails(@PathVariable String beatHash) {
        //TODO MB: Tu trzeba będzie sprawdzać czy jak bit/content nie ma visibility public to czy użeytkowniik ma do niego dostęp
        BeatDetailsDto beatDetailsDto = beatService.getBeatDetails(beatHash);
        return ResponseEntity.ok().body(beatDetailsDto);
    }

//    @GetMapping("/{beatHash}")
//    public ResponseEntity<BeatDetailsDTO> getBeatDetails(@PathVariable String beatHash) {
////        String userHash = requestContext.getUserHash();
////        BeatDetailsDTO beatDetails = beatService.getBeatDetails(beatHash, userHash);
////        return ResponseEntity.ok(beatDetails);
//        return null;
//    }
//
//    @GetMapping("/paged")
//    public ResponseEntity<CollectionWrapper<BeatSummaryDTO>> getAllBeatsPaged(
////            @RequestParam(defaultValue = "DISCOVER") FeedType feedType,
//            @PageableDefault(size = 40) Pageable pageable
//    ) {
////        Set<BeatSummaryDTO> beatSummaries = beatService.getBeatSummaries(feedType, pageable);
////        return ResponseEntity.ok(CollectionWrapper.of(beatSummaries));
//        return null;
//    }
}

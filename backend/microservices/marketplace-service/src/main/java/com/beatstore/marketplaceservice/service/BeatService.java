package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.client.UserClient;
import com.beatstore.marketplaceservice.common.enums.ContentVisibility;
import com.beatstore.marketplaceservice.common.enums.FeedType;
import com.beatstore.marketplaceservice.common.enums.FileType;
import com.beatstore.marketplaceservice.dto.BeatSummaryDTO;
import com.beatstore.marketplaceservice.dto.BeatUploadCommand;
import com.beatstore.marketplaceservice.dto.UserInfoDTO;
import com.beatstore.marketplaceservice.exceptions.MissingRequiredFileException;
import com.beatstore.marketplaceservice.model.Beat;
import com.beatstore.marketplaceservice.model.BeatLicense;
import com.beatstore.marketplaceservice.model.License;
import com.beatstore.marketplaceservice.model.MediaFile;
import com.beatstore.marketplaceservice.repository.BeatLicenseRepository;
import com.beatstore.marketplaceservice.repository.BeatRepository;
import com.beatstore.marketplaceservice.repository.LicenseRepository;
import com.beatstore.marketplaceservice.repository.MediaFileRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BeatService {
    private final BeatRepository beatRepository;
    private final LicenseRepository licenseRepository;
    private final BeatLicenseRepository beatLicenseRepository;
    private final MediaFileRepository mediaFileRepository;
    private final FileStorageService fileStorageService;
    private final UserClient userClient;

    public BeatService(BeatRepository beatRepository, LicenseRepository licenseRepository, BeatLicenseRepository beatLicenseRepository, MediaFileRepository mediaFileRepository, FileStorageService fileStorageService, UserClient userClient) {
        this.beatRepository = beatRepository;
        this.licenseRepository = licenseRepository;
        this.beatLicenseRepository = beatLicenseRepository;
        this.mediaFileRepository = mediaFileRepository;
        this.fileStorageService = fileStorageService;
        this.userClient = userClient;
    }

    @Transactional
    public void uploadNewBeat(BeatUploadCommand beatUploadCommand, MultipartFile mp3, MultipartFile untaggedWav, MultipartFile stems) {
        Beat beat = new Beat(beatUploadCommand);
        String hash = UUID.randomUUID().toString();
        beat.setHash(hash);
        Beat savedBeat = beatRepository.save(beat);

        Set<License> licenses = licenseRepository.findAllByHashIn(beatUploadCommand.getLicenseHashes());

        Set<BeatLicense> beatLicenses = new HashSet<>();
        licenses.forEach(license -> {
            beatLicenses.add(
                    new BeatLicense(savedBeat, license, beatUploadCommand.getCustomPrice())
            );
        });
        beatLicenseRepository.saveAll(beatLicenses);

        if (Objects.nonNull(untaggedWav)) {
            handleFile(untaggedWav, FileType.BEAT_WAV_UNTAGGED, savedBeat);
        } else {
            throw new MissingRequiredFileException(FileType.BEAT_WAV_UNTAGGED.name());
        }
        if (Objects.nonNull(mp3)) handleFile(mp3, FileType.BEAT_MP3, savedBeat);
        if (Objects.nonNull(stems)) handleFile(stems, FileType.BEAT_STEMS, savedBeat);
    }

    private void handleFile(MultipartFile file, FileType type, Beat beat) {
        String fileUrl = fileStorageService.upload(file);
        MediaFile media = new MediaFile();
        media.setBeat(beat);
        media.setFileType(type);
        media.setFileUrl(fileUrl);
        media.setMimeType(file.getContentType());
        media.setFileSize((int) file.getSize());
        media.setUploadedAt(LocalDateTime.now());
        mediaFileRepository.save(media);
    }

    public Set<BeatSummaryDTO> getBeatSummaries(FeedType feedType, Pageable pageable) {
        Set<Beat> beats = new HashSet<>();
        switch (feedType) {
            case NEW -> beats = beatRepository.findAllByVisibilityOrderByCreatedAtDesc(ContentVisibility.PUBLIC, pageable);
            case DISCOVER -> beats = beatRepository.findAllByVisibility(ContentVisibility.PUBLIC, pageable);
        }
        Set<String> userHashes = beats.stream().map(Beat::getUserHash).collect(Collectors.toSet());
        Map<String, UserInfoDTO> userHashToUserInfo = userClient.getUserInfo(userHashes).stream().collect(Collectors.toMap(UserInfoDTO::getUserHash, Function.identity()));
        return beats.stream()
                .map(beat ->
                        new BeatSummaryDTO(
                                beat,
                                userHashToUserInfo.get(beat.getUserHash())
                        ))
                .collect(Collectors.toSet());
    }
}

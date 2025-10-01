package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.common.enums.FileType;
import com.beatstore.marketplaceservice.dto.BeatUploadCommand;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BeatService {
    private final BeatRepository beatRepository;
    private final LicenseRepository licenseRepository;
    private final BeatLicenseRepository beatLicenseRepository;
    private final MediaFileRepository mediaFileRepository;
    private final FileStorageService fileStorageService;

    public BeatService(BeatRepository beatRepository, LicenseRepository licenseRepository, BeatLicenseRepository beatLicenseRepository, MediaFileRepository mediaFileRepository, FileStorageService fileStorageService) {
        this.beatRepository = beatRepository;
        this.licenseRepository = licenseRepository;
        this.beatLicenseRepository = beatLicenseRepository;
        this.mediaFileRepository = mediaFileRepository;
        this.fileStorageService = fileStorageService;
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
}

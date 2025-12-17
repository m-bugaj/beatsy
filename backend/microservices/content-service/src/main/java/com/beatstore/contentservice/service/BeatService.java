package com.beatstore.contentservice.service;

import com.beatstore.contentservice.dto.BeatDetailsDto;
import com.beatstore.contentservice.dto.BeatRequest;
import com.beatstore.contentservice.exceptions.BeatNotFoundException;
import com.beatstore.contentservice.model.BeatDetails;
import com.beatstore.contentservice.model.Genre;
import com.beatstore.contentservice.repository.BeatDetailsRepository;
import com.beatstore.contentservice.repository.ContentRepository;
import com.beatstore.contentservice.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BeatService {
    private final ContentRepository contentRepository;
    private final BeatDetailsRepository beatDetailsRepository;
//    private final BeatRepository beatRepository;
//    private final LicenseRepository licenseRepository;
//    private final BeatLicenseRepository beatLicenseRepository;
//    private final MediaFileRepository mediaFileRepository;
//    private final FileStorageService fileStorageService;
//    private final UserClient userClient;
    private final GenreRepository genreRepository;

    public BeatService(ContentRepository contentRepository, BeatDetailsRepository beatDetailsRepository, GenreRepository genreRepository) {
        this.contentRepository = contentRepository;
        this.beatDetailsRepository = beatDetailsRepository;
        this.genreRepository = genreRepository;
    }

    //TODO MB: W tej metodzie powinny być również wysyłane eventy na kafke do marketplace w celu dodania ogłoszenia oraz przypisania licencji do beatu
    //TODO MB: Pliki nie powinny być ani tutaj zapisywane, anie przechodzić między aplikacjami, front powinien najpierw strzelić do file-upload lub prosto do S3/GCS przez pre-signed URL (lepsza opcja)!!
    //Front KOLEJNO:
    //(1) pyta ContentService o pre-signed URL do uploadu plików
    //(2) przesyła pliki bezpośrednio do FileUploadService lub S3
    //(3) dopiero po poprawnym uploadzie pliku wysyła do ContentService metadane beatu + referencję do
    @Transactional
    public void uploadNewBeat(BeatRequest beatRequest) {
        Set<Genre> genres = genreRepository.findAllByNameIn(beatRequest.getGenres());
        BeatDetails beat = new BeatDetails(beatRequest, genres);
        String hash = UUID.randomUUID().toString();
        beat.getContent().setHash(hash);
        BeatDetails savedBeat = beatDetailsRepository.save(beat);
//        Set<License> licenses = licenseRepository.findAllByHashIn(beatUploadCommand.getLicenseHashes());
//        Set<BeatLicense> beatLicenses = new HashSet<>();

//        licenses.forEach(license -> {
//            beatLicenses.add(
//                    new BeatLicense(savedBeat, license, beatUploadCommand.getCustomPrice())
//            );
//        });
//        beatLicenseRepository.saveAll(beatLicenses);

//        if (Objects.nonNull(untaggedWav)) {
//            handleFile(untaggedWav, FileType.BEAT_WAV_UNTAGGED, savedBeat);
//        } else {
//            throw new MissingRequiredFileException(FileType.BEAT_WAV_UNTAGGED.name());
//        }
//        if (Objects.nonNull(mp3)) handleFile(mp3, FileType.BEAT_MP3, savedBeat);
//        if (Objects.nonNull(stems)) handleFile(stems, FileType.BEAT_STEMS, savedBeat);
    }

    @Transactional
    public void updateBeat(String contentHash, BeatRequest beatRequest) {
        BeatDetails beat = beatDetailsRepository.findByContent_Hash(contentHash)
                .orElseThrow(() -> new BeatNotFoundException(beatRequest.getUserHash(), contentHash));
        Set<Genre> updatedGenres = getUpdatedGenres(beatRequest, beat);
        beat.update(beatRequest, updatedGenres);
        beatDetailsRepository.save(beat);
    }

    private Set<Genre> getUpdatedGenres(BeatRequest beatRequest, BeatDetails beat) {
        Set<Genre> genres;
        if (modifiedGenres(beatRequest, beat)) {
            genres = genreRepository.findAllByNameIn(beatRequest.getGenres());
        } else {
            genres = beat.getContent().getGenres();
        }
        return genres;
    }

    private static boolean modifiedGenres(BeatRequest beatRequest, BeatDetails beat) {
        return !beatRequest.getGenres().equals(beat.getContent().getGenresNames());
    }

    public BeatDetailsDto getBeatDetails(String contentHash) {
        BeatDetails beat = beatDetailsRepository.findByContent_Hash(contentHash)
                .orElseThrow(() -> new BeatNotFoundException(contentHash));
        return new BeatDetailsDto(beat);
    }

//    private void handleFile(MultipartFile file, FileType type, Beat beat) {
//        String fileUrl = fileStorageService.upload(file);
//        MediaFile media = new MediaFile();
//        media.setBeat(beat);
//        media.setFileType(type);
//        media.setFileUrl(fileUrl);
//        media.setMimeType(file.getContentType());
//        media.setFileSize((int) file.getSize());
//        media.setUploadedAt(LocalDateTime.now());
//        mediaFileRepository.save(media);
//    }

//    public Set<BeatSummaryDTO> getBeatSummaries(FeedType feedType, Pageable pageable) {
//        Set<Beat> beats = getBeatsByFeedType(feedType, pageable);
//        Set<String> userHashes = beats.stream().map(Beat::getUserHash).collect(Collectors.toSet());
//        Map<String, UserInfoDTO> userHashToUserInfo = userClient.getUserInfo(userHashes)
//                .getContent()
//                .stream()
//                .collect(Collectors.toMap(UserInfoDTO::getUserHash, Function.identity()));
//        return beats.stream()
//                .map(beat ->
//                        new BeatSummaryDTO(
//                                beat,
//                                userHashToUserInfo.get(beat.getUserHash())
//                        ))
//                .collect(Collectors.toSet());
//    }
//
//    private Set<Beat> getBeatsByFeedType(FeedType feedType, Pageable pageable) {
//        Set<Beat> beats;
//        switch (feedType) {
//            case NEW -> beats = beatRepository.findAllByVisibilityOrderByCreatedAtDesc(ContentVisibility.PUBLIC, pageable).toSet();
//            case DISCOVER -> beats = beatRepository.findAllByVisibility(ContentVisibility.PUBLIC, pageable).toSet();
//            default -> beats = beatRepository.findAllByVisibility(ContentVisibility.PUBLIC, pageable).toSet();
//        }
//        return beats;
//    }
//
//    public BeatDetailsDTO getBeatDetails(String userHash, String beatHash) {
//        Beat beat = beatRepository.findFirstByUserHashAndHash(userHash, beatHash)
//                .orElseThrow(() -> new BeatNotFoundException(userHash, beatHash));
//        UserInfoDTO userInfo = userClient.getUserInfo(Collections.singleton(beat.getUserHash()))
//                .getContent()
//                .stream()
//                .findFirst()
//                .orElseThrow(() -> new UserNotFoundException(userHash));
//        return new BeatDetailsDTO(beat, userInfo);
//    }
}

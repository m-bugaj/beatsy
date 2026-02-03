package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.client.ContentClient;
import com.beatstore.marketplaceservice.common.enums.content.ContentType;
import com.beatstore.marketplaceservice.dto.AssignLicenseCommand;
import com.beatstore.marketplaceservice.dto.ContentBaseDto;
import com.beatstore.marketplaceservice.dto.LicenseCommand;
import com.beatstore.marketplaceservice.dto.LicenseSummaryDTO;
import com.beatstore.marketplaceservice.model.*;
import com.beatstore.marketplaceservice.repository.ContentLicenseRepository;
import com.beatstore.marketplaceservice.repository.LicenseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;
    private final ContentLicenseRepository contentLicenseRepository;
    private final ContentClient contentClient;

    public LicenseService(LicenseRepository licenseRepository, ContentLicenseRepository contentLicenseRepository, ContentClient contentClient) {
        this.licenseRepository = licenseRepository;
        this.contentLicenseRepository = contentLicenseRepository;
        this.contentClient = contentClient;
    }
    //TODO: W PZYSZŁOŚCI DODAĆ OBSŁUGĘ LIMITÓW (NP MAX 4 LICENCJE NA UŻYTKOWNIKA I DODANIE 5 MA RZUCIĆ BŁĘDEM)
    @Transactional
    public void createLicense(LicenseCommand licenseCommand) {
        log.info("Creating license for userHash: {}", licenseCommand.getUserHash());
        LicenseLimitConfig licenseLimitConfig = new LicenseLimitConfig(licenseCommand);
        License license = License.builder()
                .name(licenseCommand.getName())
                .defaultPrice(licenseCommand.getDefaultPrice())
                .userHash(licenseCommand.getUserHash())
                .licenseLimitConfig(licenseLimitConfig)
                .hash(UUID.randomUUID().toString())
                .build();
        licenseRepository.save(license);
        log.info("License saved with hash: {}", license.getHash());
        // TODO MB: Zastanowić się jak to powinno być. Ma być jakiś limit licencji w stanie active dla użytkownika / contentu? Musi być jakaś walidacja co do ilości posiadanych licencji (to będzie zależne od rodzaju subskrypcji)
        applyLicenseToAllBeatsIfRequested(licenseCommand.getUserHash(), licenseCommand.getApplyToAllBeats(), license);
    }

    private void applyLicenseToAllBeatsIfRequested(String userHash, Boolean applyToAllBeats, License license) {
        if (Objects.isNull(applyToAllBeats)
                || Boolean.FALSE.equals(applyToAllBeats)) {
            return;
        }

        Set<String> contentHashesWithAlreadyAppliedLicense = Optional.ofNullable(license.getContentLicenses())
                .orElse(Collections.emptySet())
                .stream()
                .map(ContentLicense::getContentHash)
                .collect(Collectors.toSet());

        log.info("applyToAllBeats=true, assigning license to all beats for userHash: {}", userHash);
        Collection<ContentBaseDto> content = contentClient.getContentForUserHash(userHash, ContentType.BEAT).getContent();
        Set<ContentLicense> contentLicenses = content.stream()
                .filter(c -> !contentHashesWithAlreadyAppliedLicense.contains(c.getContentHash()))
                .map(c -> new ContentLicense(c.getContentHash(), license, true, null))
                .collect(Collectors.toSet());
        contentLicenseRepository.saveAll(contentLicenses);
        log.info("Assigned license to {} content", contentLicenses.size());
    }

    public Set<LicenseSummaryDTO> getLicenseSummaries(String userHash) {
        Set<License> licenses = licenseRepository.findAllByUserHash(userHash);
        return licenses.stream()
                .map(l -> new LicenseSummaryDTO(l.getName(), l.getDefaultPrice()))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void assignLicensesToContent(AssignLicenseCommand command) {
        Set<License> licenses = licenseRepository.findAllByUserHashAndHashIn(
                command.getUserHash(),
                command.getLicenseHashToCustomPrice().keySet()
        );
        Set<ContentLicense> contentLicenses = licenses.stream()
                .map(license -> new ContentLicense(
                                command.getContentHash(),
                                license,
                                true,
                                command.getLicenseHashToCustomPrice().get(license.getHash())
                        )
                )
                .collect(Collectors.toSet());
        log.info(
                "Assigning {} licenses to content={}, user={}",
                licenses.size(),
                command.getContentHash(),
                command.getUserHash()
        );
        contentLicenseRepository.saveAll(contentLicenses);
    }
}

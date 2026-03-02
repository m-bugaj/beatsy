package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.client.ContentClient;
import com.beatstore.marketplaceservice.common.enums.content.ContentType;
import com.beatstore.marketplaceservice.dto.AssignLicenseCommand;
import com.beatstore.marketplaceservice.dto.ContentBaseDto;
import com.beatstore.marketplaceservice.dto.LicenseCommand;
import com.beatstore.marketplaceservice.dto.LicenseSummaryDTO;
import com.beatstore.marketplaceservice.model.*;
import com.beatstore.marketplaceservice.repository.ContentOfferRepository;
import com.beatstore.marketplaceservice.repository.LicenseTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LicenseService {
    private final LicenseTemplateRepository licenseTemplateRepository;
    private final ContentOfferRepository contentOfferRepository;
    private final ContentClient contentClient;

    public LicenseService(LicenseTemplateRepository licenseTemplateRepository, ContentOfferRepository contentOfferRepository, ContentClient contentClient) {
        this.licenseTemplateRepository = licenseTemplateRepository;
        this.contentOfferRepository = contentOfferRepository;
        this.contentClient = contentClient;
    }
    //TODO: W PZYSZŁOŚCI DODAĆ OBSŁUGĘ LIMITÓW (NP MAX 4 LICENCJE NA UŻYTKOWNIKA I DODANIE 5 MA RZUCIĆ BŁĘDEM)
    @Transactional
    public void createLicense(LicenseCommand licenseCommand) {
        log.info("Creating license for userHash: {}", licenseCommand.getUserHash());
        LicenseLimitConfig licenseLimitConfig = new LicenseLimitConfig(licenseCommand);
        LicenseTemplate licenseTemplate = LicenseTemplate.builder()
                .name(licenseCommand.getName())
                .defaultPrice(licenseCommand.getDefaultPrice())
                .sellerHash(licenseCommand.getUserHash())
                .licenseLimitConfig(licenseLimitConfig)
                .hash(UUID.randomUUID().toString())
                .build();
        licenseTemplateRepository.save(licenseTemplate);
        log.info("License saved with hash: {}", licenseTemplate.getHash());
        // TODO MB: Zastanowić się jak to powinno być. Ma być jakiś limit licencji w stanie active dla użytkownika / contentu? Musi być jakaś walidacja co do ilości posiadanych licencji (to będzie zależne od rodzaju subskrypcji)
        applyLicenseToAllBeatsIfRequested(licenseCommand.getUserHash(), licenseCommand.getApplyToAllBeats(), licenseTemplate);
    }

    private void applyLicenseToAllBeatsIfRequested(String userHash, Boolean applyToAllBeats, LicenseTemplate licenseTemplate) {
        if (Objects.isNull(applyToAllBeats)
                || Boolean.FALSE.equals(applyToAllBeats)) {
            return;
        }

        Set<String> contentHashesWithAlreadyAppliedLicense = Optional.ofNullable(licenseTemplate.getContentOffers())
                .orElse(Collections.emptySet())
                .stream()
                .map(ContentOffer::getContentHash)
                .collect(Collectors.toSet());

        log.info("applyToAllBeats=true, assigning license to all beats for userHash: {}", userHash);
        Collection<ContentBaseDto> content = contentClient.getContentForUserHash(userHash, ContentType.BEAT).getContent();
        Set<ContentOffer> contentLicens = content.stream()
                .filter(c -> !contentHashesWithAlreadyAppliedLicense.contains(c.getContentHash()))
                .map(c -> new ContentOffer(c.getContentHash(), licenseTemplate, true, null))
                .collect(Collectors.toSet());
        contentOfferRepository.saveAll(contentLicens);
        log.info("Assigned license to {} content", contentLicens.size());
    }

    public Set<LicenseSummaryDTO> getLicenseSummaries(String userHash) {
        Set<LicenseTemplate> licens = licenseTemplateRepository.findAllBySellerHash(userHash);
        return licens.stream()
                .map(l -> new LicenseSummaryDTO(l.getName(), l.getDefaultPrice()))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void assignLicensesToContent(AssignLicenseCommand command) {
        Set<LicenseTemplate> licens = licenseTemplateRepository.findAllBySellerHashAndHashIn(
                command.getUserHash(),
                command.getLicenseHashToCustomPrice().keySet()
        );
        Set<ContentOffer> contentLicens = licens.stream()
                .map(licenseTemplate -> new ContentOffer(
                                command.getContentHash(),
                        licenseTemplate,
                                true,
                                command.getLicenseHashToCustomPrice().get(licenseTemplate.getHash())
                        )
                )
                .collect(Collectors.toSet());
        log.info(
                "Assigning {} licenses to content={}, user={}",
                licens.size(),
                command.getContentHash(),
                command.getUserHash()
        );
        contentOfferRepository.saveAll(contentLicens);
    }
}

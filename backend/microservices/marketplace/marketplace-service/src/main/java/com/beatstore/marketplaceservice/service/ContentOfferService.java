package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.dto.ContentOffersPricesValidationResult;
import com.beatstore.marketplaceservice.dto.FetchContentOffersPricesCommand;
import com.beatstore.marketplaceservice.dto.ValidateContentOffersPricesCommand;
import com.beatstore.marketplaceservice.model.ContentOffer;
import com.beatstore.marketplaceservice.repository.ContentOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentOfferService {
    private final ContentOfferRepository contentOfferRepository;
    private final LicenseTemplateService licenseTemplateService;

    public ContentOfferService(ContentOfferRepository contentOfferRepository, LicenseTemplateService licenseTemplateService) {
        this.contentOfferRepository = contentOfferRepository;
        this.licenseTemplateService = licenseTemplateService;
    }

    public Map<String, BigDecimal> getContentOffersPrices(FetchContentOffersPricesCommand command) {
        Set<String> contentOfferHashes = command.getContentOfferHashes();
        Set<ContentOffer> contentOffers = contentOfferRepository.findAllByHashInAndActiveIsTrue(contentOfferHashes);
        Map<String, BigDecimal> licenseHashToDefaultPrice = licenseTemplateService.getLicenseHashToDefaultPrice(contentOffers);
        Map<String, BigDecimal> contentOfferHashToPrice = new HashMap<>();
        contentOffers.forEach(contentOffer -> {
            BigDecimal finalPrice = Optional.ofNullable(contentOffer.getCustomPrice())
                    .orElse(licenseHashToDefaultPrice.get(contentOffer.getLicenseTemplate().getHash()));
            contentOfferHashToPrice.put(contentOffer.getHash(), finalPrice);
        });
        return contentOfferHashToPrice;
    }

    public ContentOffersPricesValidationResult validatePricesForContentOffers(ValidateContentOffersPricesCommand command) {
        Set<String> contentOfferHashes = command.getContentOfferHashToPriceInCart().keySet();
        Map<String, BigDecimal> contentOfferHashToPrice =
                getContentOffersPrices(new FetchContentOffersPricesCommand(contentOfferHashes));
        Set<String> unavailableContentOffersHashes = new HashSet<>(contentOfferHashes);
        unavailableContentOffersHashes.removeAll(contentOfferHashToPrice.keySet());

        Set<ContentOffersPricesValidationResult.PriceChange> priceChanges = contentOfferHashToPrice.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(command.getContentOfferHashToPriceInCart().get(entry.getKey())) != 0)
                .map(entry -> new ContentOffersPricesValidationResult.PriceChange(entry.getKey(), command.getContentOfferHashToPriceInCart().get(entry.getKey()), entry.getValue()))
                .collect(Collectors.toSet());

        boolean allValid = priceChanges.isEmpty() && unavailableContentOffersHashes.isEmpty();
        return new ContentOffersPricesValidationResult(allValid, unavailableContentOffersHashes, priceChanges);
    }
}



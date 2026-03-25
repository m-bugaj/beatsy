package com.beatstore.marketplaceservice.service;

import com.beatstore.marketplaceservice.dto.FetchContentOffersPricesCommand;
import com.beatstore.marketplaceservice.model.ContentOffer;
import com.beatstore.marketplaceservice.repository.ContentOfferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
}

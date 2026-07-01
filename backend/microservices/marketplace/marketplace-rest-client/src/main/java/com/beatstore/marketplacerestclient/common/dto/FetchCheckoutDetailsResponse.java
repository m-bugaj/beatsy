package com.beatstore.marketplacerestclient.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class FetchCheckoutDetailsResponse {
    private Set<ContentOfferCheckoutDetails> checkoutDetails;
}

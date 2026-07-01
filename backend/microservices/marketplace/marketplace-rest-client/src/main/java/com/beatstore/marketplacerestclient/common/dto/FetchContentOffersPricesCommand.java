package com.beatstore.marketplacerestclient.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class FetchContentOffersPricesCommand {
    private Set<String> contentOfferHashes;
}

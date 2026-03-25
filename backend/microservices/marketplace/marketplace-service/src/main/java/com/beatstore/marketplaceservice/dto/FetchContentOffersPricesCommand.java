package com.beatstore.marketplaceservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Getter
public class FetchContentOffersPricesCommand {
    private Set<String> contentOfferHashes;
}

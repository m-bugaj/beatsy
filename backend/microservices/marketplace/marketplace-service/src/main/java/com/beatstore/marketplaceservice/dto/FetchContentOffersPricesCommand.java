package com.beatstore.marketplaceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FetchContentOffersPricesCommand {
    private Set<String> contentOfferHashes;
}

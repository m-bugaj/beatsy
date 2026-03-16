package com.beatstore.orderservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
public class AddItemsToCartCommand {
    // Na razie system nie przewiduje zakupów wielosztukowych
//    Map<String, Integer> contentOfferHashToQuantity;

    Set<String> contentOfferHashes;
}

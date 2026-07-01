package com.beatstore.marketplacerestclient.common.dto;

import com.beatstore.marketplacerestclient.common.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ContentOfferCheckoutDetails {
    private String contentOfferHash;
    private String sellerHash;
    private ContentType contentType;
    private String contentHash;
    private String contentName;
    private String licenseName;
    private String licenseHash;
}

package com.beatstore.marketplaceservice.common.enums;

public enum ContentVisibility {
    PUBLIC,      // visible to everyone, listed in marketplace
    PRIVATE,     // visible only to the owner
    UNLISTED,    // hidden from listings, accessible via direct link
    PROTECTED    // accessible only after purchase / with special permissions
}
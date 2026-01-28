package com.beatstore.marketplaceservice.common.enums;

public enum MimeType {
    // Images
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_GIF("image/gif"),
    IMAGE_WEBP("image/webp"),
    IMAGE_SVG("image/svg+xml"),

    // Audio
    AUDIO_MPEG("audio/mpeg"),
    AUDIO_WAV("audio/wav"),
    AUDIO_OGG("audio/ogg"),

//    // Video
//    VIDEO_MP4("video/mp4"),
//    VIDEO_WEBM("video/webm"),
//    VIDEO_OGG("video/ogg"),

    // Documents
    APPLICATION_PDF("application/pdf"),
    APPLICATION_ZIP("application/zip"),
    APPLICATION_JSON("application/json"),
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),

    // Fallback
    UNKNOWN("application/octet-stream");

    private final String type;

    MimeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static MimeType fromString(String mimeType) {
        for (MimeType type : values()) {
            if (type.type.equalsIgnoreCase(mimeType)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}

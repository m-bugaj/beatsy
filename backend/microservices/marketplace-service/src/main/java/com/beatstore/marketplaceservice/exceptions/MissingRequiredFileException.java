package com.beatstore.marketplaceservice.exceptions;

public class MissingRequiredFileException extends RuntimeException {
    public MissingRequiredFileException(String requiredFile) {

        super(String.format("Missing required file: %s", requiredFile));
    }
}

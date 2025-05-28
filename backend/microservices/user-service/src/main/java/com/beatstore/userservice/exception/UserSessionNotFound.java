package com.beatstore.userservice.exception;

public class UserSessionNotFound extends RuntimeException {
    public UserSessionNotFound(String userHash) {

        super(String.format("Session not found for user: {%s}", userHash));
    }
}

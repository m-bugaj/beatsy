package com.beatstore.userservice.exception;

public class UserSessionExpired extends RuntimeException {
    public UserSessionExpired(String userHash) {

        super(String.format("Session expired for user: {%s}", userHash));
    }
}

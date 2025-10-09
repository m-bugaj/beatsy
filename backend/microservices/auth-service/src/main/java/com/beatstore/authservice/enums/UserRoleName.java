package com.beatstore.authservice.enums;

import java.util.Set;

public enum UserRoleName {
    USER,
    BUYER,
    ADMIN;

    private static final Set<UserRoleName> DEFAULT_ROLES = Set.of(USER, BUYER);

    public static Set<UserRoleName> getDefaultRoles() {
        return DEFAULT_ROLES;
    }
}

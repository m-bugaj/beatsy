package com.beatstore.authservice.dto;

import lombok.*;

@Builder
public class UserInfoDTO {
    private String userHash;
    private String username;
}

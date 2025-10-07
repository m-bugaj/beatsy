package com.beatstore.userservice.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class UserInfoCommand {
    Set<String> userHashes;
}

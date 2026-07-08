package com.beatstore.contentrestclient.dto;

import com.beatstore.contentrestclient.common.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ContentForUserResponse {
    private String contentHash;
    private String userHash;
    private ContentType type;
    private String title;
    private String description;
    private String visibility;
    private Set<String> genres = new HashSet<>();
}

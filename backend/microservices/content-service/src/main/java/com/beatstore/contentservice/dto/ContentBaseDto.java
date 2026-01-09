package com.beatstore.contentservice.dto;

import com.beatstore.contentservice.common.enums.ContentType;
import com.beatstore.contentservice.common.enums.ContentVisibility;
import com.beatstore.contentservice.model.Content;
import com.beatstore.contentservice.model.Genre;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ContentBaseDto {
    private String contentHash;
    private String userHash;
    private ContentType type;
    private String title;
    private String description;
    private ContentVisibility visibility;
    private Set<Genre> genres = new HashSet<>();

    public ContentBaseDto(Content content) {
        this.contentHash = content.getHash();
        this.userHash = content.getUserHash();
        this.type = content.getType();
        this.title = content.getTitle();
        this.description = content.getDescription();
        this.visibility = content.getVisibility();
        this.genres = content.getGenres();
    }
}

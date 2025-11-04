package com.beatstore.marketplaceservice.dto;

import com.beatstore.marketplaceservice.common.enums.ContentVisibility;
import com.beatstore.marketplaceservice.common.enums.beat.BeatGenre;
import com.beatstore.marketplaceservice.common.enums.beat.BeatMood;
import com.beatstore.marketplaceservice.model.Beat;

import java.util.Set;

public class BeatDetailsDTO {
    private String title;
    private String author;
    private String description;
    private Integer bpm;
    private Set<BeatGenre> genres;
    private Set<BeatMood> moods;
    private ContentVisibility visibility;

    public BeatDetailsDTO(Beat beat, UserInfoDTO userInfo) {
        this.title = beat.getTitle();
        this.author = userInfo.getDisplayName();
        this.description = beat.getDescription();
        this.bpm = beat.getBpm();
        this.genres = beat.getGenresNames();
    }
}

package com.beatstore.contentservice.dto;

import com.beatstore.marketplaceservice.common.enums.ContentVisibility;
import com.beatstore.marketplaceservice.model.Beat;


public class BeatSummaryDTO {
    private String title;
    private ContentVisibility visibility;
    private String author;

    public BeatSummaryDTO(Beat beat, UserInfoDTO userInfo) {
        this.title = beat.getTitle();
        this.visibility = beat.getVisibility();
        this.author = userInfo.getDisplayName();
    }
}

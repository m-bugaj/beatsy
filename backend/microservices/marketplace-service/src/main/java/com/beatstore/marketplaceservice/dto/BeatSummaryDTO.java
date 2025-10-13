package com.beatstore.marketplaceservice.dto;

import com.beatstore.marketplaceservice.common.enums.ContentVisibility;
import com.beatstore.marketplaceservice.model.Beat;
import lombok.Builder;


@Builder
public class BeatSummaryDTO {
    private String title;
    private ContentVisibility visibility;
    private String author;

    public BeatSummaryDTO(Beat beat, UserInfoDTO userInfo) {
        this.title = beat.getTitle();
        this.visibility = beat.getVisibility();
        this.author = userInfo.getUsername();
    }
}

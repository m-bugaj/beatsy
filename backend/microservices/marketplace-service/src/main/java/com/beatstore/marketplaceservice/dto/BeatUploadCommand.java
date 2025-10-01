package com.beatstore.marketplaceservice.dto;


import com.beatstore.marketplaceservice.common.enums.ContentVisibility;
import com.beatstore.marketplaceservice.common.enums.beat.BeatGenre;
import com.beatstore.marketplaceservice.common.enums.beat.BeatMood;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
public class BeatUploadCommand {
    @JsonIgnore
    @Setter
    private String userHash;

    private String title;
    private String description;
    private Integer bpm;
    private BeatGenre genre;
    private BeatMood mood;
    private ContentVisibility visibility;

    private Set<String> licenseHashes;
    private BigDecimal customPrice;
}

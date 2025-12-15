package com.beatstore.contentservice.dto;

import com.beatstore.contentservice.common.enums.ContentVisibility;
import com.beatstore.contentservice.common.enums.MusicGenre;
import com.beatstore.contentservice.common.enums.beat.BeatMood;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class BeatRequest {
    @JsonIgnore
    private String userHash;

    //CONTENT METADATA
    private String title;
    private String description;
    private Set<MusicGenre> genres;
    private ContentVisibility visibility;

    //BEAT DETAILS METADATA
    private Integer bpm;
    private BeatMood mood;
    private String key;

    //LICENSES AND PRICES (Ale to chyba powinno być w mapie licencja do custom price)
    private Set<String> licenseHashes;
    private BigDecimal customPrice;
}

package com.beatstore.contentservice.dto;

import com.beatstore.contentservice.common.enums.ContentVisibility;
import com.beatstore.contentservice.common.enums.MusicGenre;
import com.beatstore.contentservice.common.enums.beat.BeatMood;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

//TODO MB: Można rozdzielić na create i update (w create np dodać walidacje not null i not blank) - ewentualnie jakaś dodatkowa   klasa base
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
    private Map<String, BigDecimal> licenseHashToCustomPrice;
//    private Set<String> licenseHashes;
//    private BigDecimal customPrice;
}

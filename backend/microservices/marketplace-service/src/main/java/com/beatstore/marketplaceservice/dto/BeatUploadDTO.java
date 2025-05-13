package com.beatstore.marketplaceservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
public class BeatUploadDTO {
//    @JsonIgnore
    @Getter
    private String userHash;

    //TODO: PRZEROBIĆ TE STRINGI NA ENUMY
    private String title;
    private String description;
    private Integer bpm;
    private String genre;
    private String mood;
    private String visibility;

    private Set<String> licenseHashes;
    private BigDecimal customPrice;
}

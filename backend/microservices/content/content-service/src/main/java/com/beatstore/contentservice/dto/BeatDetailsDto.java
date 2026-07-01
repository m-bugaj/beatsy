package com.beatstore.contentservice.dto;

import com.beatstore.contentservice.common.enums.beat.BeatMood;
import com.beatstore.contentservice.model.BeatDetails;
import lombok.Getter;

@Getter
public class BeatDetailsDto extends ContentBaseDto {
    private Integer bpm;
    private String key;
    private BeatMood mood;

    public BeatDetailsDto(BeatDetails beatDetails) {
        super(beatDetails.getContent());
        this.bpm = beatDetails.getBpm();
        this.key = beatDetails.getKey();
        this.mood = beatDetails.getMood();
    }
}

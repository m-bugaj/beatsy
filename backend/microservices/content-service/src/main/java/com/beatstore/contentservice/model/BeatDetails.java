package com.beatstore.contentservice.model;

import com.beatstore.contentservice.common.enums.ContentType;
import com.beatstore.contentservice.common.enums.beat.BeatMood;
import com.beatstore.contentservice.dto.BeatRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "beat_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeatDetails {

    @Id
    @Column(name = "content_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "content_id")
    private Content content;

    private Integer bpm;

    @Column(name = "key")
    private String key;

    @Enumerated(EnumType.STRING)
    private BeatMood mood;

    public BeatDetails(BeatRequest command, Set<Genre> genres) {
        this.content = new Content(command.getUserHash(), ContentType.BEAT, command.getTitle(),
                command.getDescription(), command.getVisibility(), genres);
        this.bpm = command.getBpm();
        this.mood = command.getMood();
        this.key = command.getKey();
    }

    public void update(BeatRequest command, Set<Genre> genres) {
        this.bpm = command.getBpm();
        this.key = command.getKey();
        this.mood = command.getMood();
        this.content.update(command.getTitle(), command.getDescription(),
                command.getVisibility(), genres);
    }
}

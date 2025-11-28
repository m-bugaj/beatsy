package com.beatstore.contentservice.model;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @MapsId
    @JoinColumn(name = "content_id")
    private Content content;

    private Integer bpm;

    @Column(name = "key")
    private String key;

    //TODO MB: Do zmiany na enum
    private String mood;
}

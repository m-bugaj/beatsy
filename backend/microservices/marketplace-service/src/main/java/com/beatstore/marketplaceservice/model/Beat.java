package com.beatstore.marketplaceservice.model;

import com.beatstore.marketplaceservice.common.enums.ContentVisibility;
import com.beatstore.marketplaceservice.common.enums.beat.BeatGenre;
import com.beatstore.marketplaceservice.common.enums.beat.BeatMood;
import com.beatstore.marketplaceservice.dto.BeatUploadCommand;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "beats")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Beat {
    @Id
    @SequenceGenerator(
            name = "beats_id_seq",
            sequenceName = "beats_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "beats_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private String userHash;

    @Column(nullable = false)
    private String title;

    private String description;
    private Integer bpm;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BeatGenre genre;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BeatMood mood;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentVisibility visibility;

    @OneToMany(mappedBy = "beat")
    private Set<MediaFile> mediaFiles = new HashSet<>();

    @OneToMany(mappedBy = "beat")
    private Set<BeatLicense> beatLicenses = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Beat(BeatUploadCommand beatUploadCommand) {
        this.userHash = beatUploadCommand.getUserHash();
        this.title = beatUploadCommand.getTitle();
        this.description = beatUploadCommand.getDescription();
        this.bpm = beatUploadCommand.getBpm();
        this.genre = beatUploadCommand.getGenre();
        this.mood = beatUploadCommand.getMood();
        this.visibility = beatUploadCommand.getVisibility();
    }
}

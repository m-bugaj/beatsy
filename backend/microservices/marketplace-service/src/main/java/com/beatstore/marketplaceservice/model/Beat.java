package com.beatstore.marketplaceservice.model;

import com.beatstore.marketplaceservice.dto.BeatUploadDTO;
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
    private String genre;

    @Column(nullable = false)
    private String mood;

    @Column(nullable = false)
    private String visibility;

    @OneToMany(mappedBy = "beat")
    private Set<MediaFile> mediaFiles = new HashSet<>();

    @OneToMany(mappedBy = "beat")
    private Set<BeatLicense> beatLicenses = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Beat(BeatUploadDTO beatUploadDTO) {
        this.userHash = beatUploadDTO.getUserHash();
        this.title = beatUploadDTO.getTitle();
        this.description = beatUploadDTO.getDescription();
        this.bpm = beatUploadDTO.getBpm();
        this.genre = beatUploadDTO.getGenre();
        this.mood = beatUploadDTO.getMood();
        this.visibility = beatUploadDTO.getVisibility();
    }
}

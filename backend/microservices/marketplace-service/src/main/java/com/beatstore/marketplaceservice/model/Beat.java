package com.beatstore.marketplaceservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "beats")
@Getter
@Setter
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

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

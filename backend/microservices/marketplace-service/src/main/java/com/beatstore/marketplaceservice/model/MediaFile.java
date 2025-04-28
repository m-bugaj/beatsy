package com.beatstore.marketplaceservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "media_files")
@Getter
@Setter
public class MediaFile {
    @Id
    @SequenceGenerator(
            name = "media_files_id_seq",
            sequenceName = "media_files_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "media_files_id_seq"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "beat_id", nullable = false)
    private Beat beat;
}

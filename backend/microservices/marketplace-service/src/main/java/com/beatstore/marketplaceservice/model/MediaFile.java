package com.beatstore.marketplaceservice.model;

import com.beatstore.marketplaceservice.common.enums.FileType;
import com.beatstore.marketplaceservice.common.enums.MimeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private MimeType mimeType;

    private Integer fileSize;
    private LocalDateTime uploadedAt;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "beat_id", nullable = false)
    private Beat beat;
}

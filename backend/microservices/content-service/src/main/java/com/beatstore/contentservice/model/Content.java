package com.beatstore.contentservice.model;

import com.beatstore.contentservice.common.enums.ContentType;
import com.beatstore.contentservice.common.enums.ContentVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "content")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    @Id
    @SequenceGenerator(
            name = "content_id_seq",
            sequenceName = "content_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "content_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private String userHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType type;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentVisibility visibility;

    @ManyToMany
    @JoinTable(
            name = "content_genres",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

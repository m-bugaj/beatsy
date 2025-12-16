package com.beatstore.contentservice.model;

import com.beatstore.contentservice.common.enums.ContentType;
import com.beatstore.contentservice.common.enums.ContentVisibility;
import com.beatstore.contentservice.common.enums.MusicGenre;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Content(String userHash, ContentType type, String title, String description, ContentVisibility visibility,
                   Set<Genre> genres) {
        this.userHash = userHash;
        this.type = type;
        this.title = title;
        this.description = description;
        this.visibility = visibility;
        this.genres = genres;
    }

    public Set<MusicGenre> getGenresNames() {
        return this.genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());
    }

    public void update(String title, String description, ContentVisibility visibility, Set<Genre> genres) {
        this.title = title;
        this.description = description;
        this.visibility = visibility;

        this.genres.clear();
        this.genres.addAll(genres);
    }

}

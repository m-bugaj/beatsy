package com.beatstore.contentservice.model;

import com.beatstore.contentservice.common.enums.MusicGenre;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "genres")
@Getter
public class Genre {

    @Id
    @SequenceGenerator(
            name = "genres_id_seq",
            sequenceName = "genres_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "genres_id_seq"
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private MusicGenre name;
}

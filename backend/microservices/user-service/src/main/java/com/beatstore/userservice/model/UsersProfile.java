package com.beatstore.userservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_profile")
public class UsersProfile {
    @Id
    @SequenceGenerator(
            name = "users_profile_id_seq",
            sequenceName = "users_profile_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_profile_id_seq"
    )
    private Long id;

    private String userHash;
    private String displayName;
    private String bio;

    //COUNTRY
    private String location;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

package com.beatstore.userservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password_hash;

    private String firstName;
    private String lastName;

    @OneToMany(
            mappedBy = "user"
    )
    private Set<UserRoles> userRoles = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

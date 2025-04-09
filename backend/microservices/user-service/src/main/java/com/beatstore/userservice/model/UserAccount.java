package com.beatstore.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_hash")
    private String userHash;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String firstName;
    private String lastName;

    @OneToMany(
            mappedBy = "userAccount"
    )
    private Set<UserRole> userRole = new HashSet<>();

//    @CreationTimestamp
    private LocalDateTime createdAt;

//    @UpdateTimestamp
//    @Column(updatable = false)
    private LocalDateTime modifiedAt;

    public UserAccount(String userHash, String username, String email, String passwordHash, String firstName, String lastName) {
        this.userHash = userHash;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserAccount(Long id, String userHash, String username, String email, String passwordHash, String firstName, String lastName, Set<UserRole> userRole, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userHash = userHash;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

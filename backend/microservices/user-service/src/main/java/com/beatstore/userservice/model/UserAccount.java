package com.beatstore.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)
    private UserSession userSession;

//    @CreationTimestamp
    private LocalDateTime createdAt;

//    @UpdateTimestamp
//    @Column(updatable = false)
    private LocalDateTime modifiedAt;

    public UserAccount(String userHash, String username, String email, String passwordHash, String firstName, String lastName, Role role) {
        this.userHash = userHash;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public UserAccount(Long id, String userHash, String username, String email, String passwordHash, String firstName, String lastName, Role role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userHash = userHash;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public UserAccount(Long id, String userHash, String username, String email, String passwordHash, String firstName, String lastName, Role role, UserSession userSession, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userHash = userHash;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.userSession = userSession;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

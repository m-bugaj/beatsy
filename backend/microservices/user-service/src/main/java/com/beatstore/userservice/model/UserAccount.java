package com.beatstore.userservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
public class UserAccount {
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
            mappedBy = "userAccount"
    )
    private Set<UserRole> userRole = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public UserAccount(String username, String email, String password_hash, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

package com.beatstore.authservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expires;

    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public RefreshToken(String token, LocalDateTime expires, UserAccount userAccount) {
        this.token = token;
        this.expires = expires;
        this.userAccount = userAccount;
    }

    public boolean isExpired() {
        return expires.isBefore(LocalDateTime.now());
    }

}

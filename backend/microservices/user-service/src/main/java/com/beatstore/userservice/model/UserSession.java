package com.beatstore.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_session")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    @Id
    @SequenceGenerator(
            name = "user_session_id_seq",
            sequenceName = "user_session_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_session_id_seq"
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private UserAccount userAccount;

    private String userHash;
    private String subscriptionHash;
    private String ipAddress;

    @Column(name = "user_agent", length = 512)
    private String userAgent;

    private LocalDateTime lastActivity;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

package com.beatstore.userservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@NoArgsConstructor
public class UserRole {
    @Id
    @SequenceGenerator(
            name = "user_role_id_seq",
            sequenceName = "user_role_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_role_id_seq"
    )
    private Long id;

    @ManyToOne()
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false
    )
    private UserAccount userAccount;

    @ManyToOne()
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public UserRole(UserAccount userAccount, Role role) {
        this.userAccount = userAccount;
        this.role = role;
    }
}

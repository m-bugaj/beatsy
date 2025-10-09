package com.beatstore.authservice.model;

import com.beatstore.authservice.enums.UserRoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleName name;

    @Column(nullable = false)
    private Boolean isDefault;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

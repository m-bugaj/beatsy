package com.beatstore.userservice.model;

import com.beatstore.userservice.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private UserRole name;

    @Column(nullable = false)
    private Boolean isDefault;

    @OneToMany(mappedBy = "role")
    private Set<UserAccount> users = new HashSet<>();


    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

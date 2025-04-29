package com.beatstore.marketplaceservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "licenses")
@Getter
@Setter
public class License {
    @Id
    @SequenceGenerator(
            name = "licenses_id_seq",
            sequenceName = "licenses_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "licenses_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal defaultPrice;

    @Column(nullable = false)
    private String userHash;

    @OneToOne
    @JoinColumn(
            name = "license_limit_config_id",
            referencedColumnName = "id",
            nullable = false
    )
    private LicenseLimitConfig licenseLimitConfig;

    @OneToMany(mappedBy = "license")
    private Set<BeatLicense> beatLicenses = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

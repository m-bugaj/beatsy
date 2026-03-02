package com.beatstore.marketplaceservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "license_template")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LicenseTemplate {
    @Id
    @SequenceGenerator(
            name = "license_template_id_seq",
            sequenceName = "license_template_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "license_template_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal defaultPrice;

    @Column(nullable = false)
    private String sellerHash;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "license_limit_config_id",
            referencedColumnName = "id",
            nullable = false
    )
    private LicenseLimitConfig licenseLimitConfig;

    @OneToMany(mappedBy = "licenseTemplate")
    private Set<ContentOffer> contentOffers = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

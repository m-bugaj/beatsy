package com.beatstore.marketplaceservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "content_offer")
@Getter
@Setter
@NoArgsConstructor
public class ContentOffer {
    @Id
    @SequenceGenerator(
            name = "content_offer_id_seq",
            sequenceName = "content_offer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "content_offer_id_seq"
    )
    private Long id;

    private String contentHash;

    @ManyToOne
    @JoinColumn(
            name = "license_template_id",
            referencedColumnName = "id",
            nullable = false
    )
    private LicenseTemplate licenseTemplate;

    private BigDecimal customPrice;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ContentOffer(String contentHash, LicenseTemplate licenseTemplate, boolean active, BigDecimal customPrice) {
        this.contentHash = contentHash;
        this.licenseTemplate = licenseTemplate;
        this.active = active;
        if (Objects.nonNull(customPrice)) {
            this.customPrice = customPrice;
        }
    }
}

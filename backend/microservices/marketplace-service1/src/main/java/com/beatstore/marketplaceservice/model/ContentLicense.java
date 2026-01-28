package com.beatstore.marketplaceservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "content_license")
@Getter
@Setter
@NoArgsConstructor
public class ContentLicense {
    @Id
    @SequenceGenerator(
            name = "content_license_id_seq",
            sequenceName = "content_license_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "content_license_id_seq"
    )
    private Long id;

    private String contentHash;

    @ManyToOne
    @JoinColumn(
            name = "license_id",
            referencedColumnName = "id",
            nullable = false
    )
    private License license;

    private BigDecimal customPrice;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ContentLicense(String contentHash, License license, boolean active, BigDecimal customPrice) {
        this.contentHash = contentHash;
        this.license = license;
        this.active = active;
        if (Objects.nonNull(customPrice)) {
            this.customPrice = customPrice;
        }
    }
}

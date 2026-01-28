package com.beatstore.marketplaceservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "beat_license")
@Getter
@Setter
@NoArgsConstructor
public class BeatLicense {
    @Id
    @SequenceGenerator(
            name = "beat_license_id_seq",
            sequenceName = "beat_license_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "beat_license_id_seq"
    )
    private Long id;

    @ManyToOne()
    @JoinColumn(
            name = "beat_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Beat beat;

    @ManyToOne()
    @JoinColumn(
            name = "license_id",
            referencedColumnName = "id",
            nullable = false
    )
    private License license;

    private BigDecimal customPrice;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public BeatLicense(Beat beat, License license, BigDecimal customPrice) {
        this.beat = beat;
        this.license = license;
        if (Objects.nonNull(customPrice)) {
            this.customPrice = customPrice;
        }
    }
}

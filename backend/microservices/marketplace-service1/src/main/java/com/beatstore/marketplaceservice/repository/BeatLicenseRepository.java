package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.model.BeatLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeatLicenseRepository extends JpaRepository<BeatLicense, Long> {
}

package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {
    Set<License> findAllByHashIn(Set<String> hashes);
}

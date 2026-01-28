package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.model.ContentLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContentLicenseRepository extends JpaRepository<ContentLicense, Long> {
    Set<ContentLicense> findAllByLicense_UserHash(String userHash);
}

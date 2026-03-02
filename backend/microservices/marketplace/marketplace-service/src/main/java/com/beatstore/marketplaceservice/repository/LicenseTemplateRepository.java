package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.model.LicenseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LicenseTemplateRepository extends JpaRepository<LicenseTemplate, Long> {
    Set<LicenseTemplate> findAllByHashIn(Set<String> hashes);

    Set<LicenseTemplate> findAllBySellerHash(String sellerHash);

    Set<LicenseTemplate> findAllBySellerHashAndHashIn(String sellerHash, Set<String> hashes);
}

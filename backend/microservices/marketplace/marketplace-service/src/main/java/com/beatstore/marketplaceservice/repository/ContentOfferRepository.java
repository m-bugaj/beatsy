package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.model.ContentOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContentOfferRepository extends JpaRepository<ContentOffer, Long> {
    Set<ContentOffer> findAllByLicenseTemplate_SellerHash(String sellerHash);
}

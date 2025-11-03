package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.common.enums.ContentVisibility;
import com.beatstore.marketplaceservice.model.Beat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BeatRepository extends JpaRepository<Beat, Long> {

    Set<Beat> findAllByUserHash(String userHash);

    Page<Beat> findAllByVisibility(ContentVisibility visibility, Pageable pageable);

    Page<Beat> findAllByVisibilityOrderByCreatedAtDesc(ContentVisibility visibility, Pageable pageable);

    Optional<Beat> findFirstByUserHashAndHash(String userHash, String beatHash);
}

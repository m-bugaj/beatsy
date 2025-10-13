package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.common.enums.ContentVisibility;
import com.beatstore.marketplaceservice.model.Beat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Set;

@Repository
public interface BeatRepository extends JpaRepository<Beat, Long> {

    Set<Beat> findAllByUserHash(String userHash);

    Set<Beat> findAllByVisibility(ContentVisibility visibility, Pageable pageable);

    Set<Beat> findAllByVisibilityOrderByCreatedAtDesc(ContentVisibility visibility, Pageable pageable);
}

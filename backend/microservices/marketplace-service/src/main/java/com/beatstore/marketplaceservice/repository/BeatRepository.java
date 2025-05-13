package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.model.Beat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeatRepository extends JpaRepository<Beat, Long> {

}

package com.beatstore.contentservice.repository;

import com.beatstore.contentservice.model.BeatDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeatDetailsRepository extends JpaRepository<BeatDetails, Integer> {
    Optional<BeatDetails> findByContent_Hash(String contentHash);
}

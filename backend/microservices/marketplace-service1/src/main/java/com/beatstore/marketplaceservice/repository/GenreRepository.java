package com.beatstore.marketplaceservice.repository;

import com.beatstore.marketplaceservice.common.enums.beat.BeatGenre;
import com.beatstore.marketplaceservice.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Set<Genre> findAllByNameIn(Set<BeatGenre> genreName);
}

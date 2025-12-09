package com.beatstore.contentservice.repository;

import com.beatstore.contentservice.common.enums.MusicGenre;
import com.beatstore.contentservice.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Set<Genre> findAllByNameIn(Set<MusicGenre> genreName);
}

package com.beatstore.contentservice.repository;

import com.beatstore.contentservice.common.enums.ContentType;
import com.beatstore.contentservice.common.enums.ContentVisibility;
import com.beatstore.contentservice.model.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Set<Content> findAllByUserHash(String userHash);

    Set<Content> findAllByUserHashAndType(String userHash, ContentType type);

    Page<Content> findAllByVisibility(ContentVisibility visibility, Pageable pageable);

    Page<Content> findAllByVisibilityOrderByCreatedAtDesc(ContentVisibility visibility, Pageable pageable);

    Optional<Content> findFirstByUserHashAndHash(String userHash, String beatHash);
}

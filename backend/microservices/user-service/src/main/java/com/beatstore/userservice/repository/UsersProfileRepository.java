package com.beatstore.userservice.repository;

import com.beatstore.userservice.model.UsersProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UsersProfileRepository extends JpaRepository<UsersProfile, Long> {
    List<UsersProfile> findAllByUserHashIn(Set<String> userHash);
}

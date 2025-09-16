package com.beatstore.userservice.repository;

import com.beatstore.userservice.enums.UserRoleName;
import com.beatstore.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRoleName name);

    Set<Role> findAllByNameIn(Set<UserRoleName> names);
}

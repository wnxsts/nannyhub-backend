package com.nannyhub.backend.repository;

import com.nannyhub.backend.entity.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ParentProfileRepository extends JpaRepository<ParentProfile, Integer> {
    Optional<ParentProfile> findByUserId(Long userId);
}

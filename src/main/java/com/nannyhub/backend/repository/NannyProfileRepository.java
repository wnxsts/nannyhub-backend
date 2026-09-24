package com.nannyhub.backend.repository;


import com.nannyhub.backend.entity.NannyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NannyProfileRepository extends JpaRepository<NannyProfile, Long> {
    Optional<NannyProfile> findByUserId(Long userId);
}

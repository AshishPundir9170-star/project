package com.sih26132.repository;

import com.sih26132.entity.Farm;
import com.sih26132.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FarmRepository extends JpaRepository<Farm, UUID> {

    List<Farm> findByUser(User user);

    Optional<Farm> findByIdAndUser(UUID id, User user);

    boolean existsByIdAndUser(UUID id, User user);
}
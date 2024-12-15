package com.group3.healthconsult.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.healthconsult.models.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findById(Long id);
    Tag findByName(String name);
    
}

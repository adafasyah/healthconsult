package com.group3.healthconsult.services;

import java.util.List;
import java.util.Optional;

import com.group3.healthconsult.dto.TagDto;
import com.group3.healthconsult.models.Tag;

public interface TagService {
    List<TagDto> getAll();
    Optional<TagDto> findById(Long id);
    Tag create(Tag tag);
    Tag update(TagDto tag);
    Tag delete(Long id);
}

package com.group3.healthconsult.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.healthconsult.dto.TagDto;
import com.group3.healthconsult.models.Tag;
import com.group3.healthconsult.repository.TagRepository;
import com.group3.healthconsult.services.TagService;

@Service
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDto> getAll() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tag -> mapToTagDto(tag)).collect(Collectors.toList());
    }

    @Override
    public Optional<TagDto> findById(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        return tag.map(value -> mapToTagDto(value));
    }

    @Override
    public Tag create(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag update(TagDto tagDto) {
        Tag tag = mapToTag(tagDto);
        return tagRepository.save(tag);
    }

    @Override
    public Tag delete(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            tagRepository.deleteById(id);
        }
        return tag.orElse(null);
    }

    private Tag mapToTag(TagDto tagDto) {
        Tag tag = Tag.builder()
                .id(tagDto.getId())
                .name(tagDto.getName())
                .createdAt(tagDto.getCreatedAt())
                .updatedAt(tagDto.getUpdatedAt())
                .build();

        return tag;
    }

    private TagDto mapToTagDto(Tag tag) {
        TagDto tagDto = TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .createdAt(tag.getCreatedAt())
                .updatedAt(tag.getUpdatedAt())
                .build();

        return tagDto;
    }
}
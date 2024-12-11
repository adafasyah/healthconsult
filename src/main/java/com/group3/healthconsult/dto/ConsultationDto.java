package com.group3.healthconsult.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.group3.healthconsult.models.ConsultationResponse;
import com.group3.healthconsult.models.Doctor;
import com.group3.healthconsult.models.Patient;
import com.group3.healthconsult.models.Specialization;
import com.group3.healthconsult.models.Tag;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ConsultationDto {
    private Long id;

    @NotEmpty(message = "Patient is required")
    private Patient patient;

    @Nullable
    private Doctor consult_to;

    @NotEmpty(message = "Specialization is required")
    private Specialization specialization;

    @NotEmpty(message = "Tags is required")
    private List<Tag> tags;

    @NotEmpty(message = "Title is required")
    private String title;

    private Boolean isResolved;

    @NotEmpty(message = "Content is required")
    private String content;

    private List<ConsultationResponse> responses;
    private Long responsesCount;
    private Long upvotesCount;
    private Long downvotesCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

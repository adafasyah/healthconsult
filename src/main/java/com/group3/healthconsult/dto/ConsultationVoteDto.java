package com.group3.healthconsult.dto;

import java.time.LocalDateTime;

import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultationVoteDto {
    private Long id;

    @NotEmpty(message = "Consultation is required")
    private Consultation consultation;

    @NotEmpty(message = "User is required")
    private User user;

    @NotEmpty(message = "Vote is required")
    private Integer vote;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

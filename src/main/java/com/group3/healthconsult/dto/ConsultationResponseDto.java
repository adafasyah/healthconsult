package com.group3.healthconsult.dto;

import java.time.LocalDateTime;

import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.ConsultationResponse;
import com.group3.healthconsult.models.Doctor;
import com.group3.healthconsult.models.User;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultationResponseDto {
    private Long id;
    
    @NotEmpty(message = "Consultation is required")
    private Consultation consultation;

    @NotEmpty(message = "User is required")
    private User user;
    
    private ConsultationResponse responseTo;
    private Boolean isDoctor;

    @Nullable
    private Doctor doctorRef;

    private Boolean isConsultationOwner;

    @NotEmpty(message = "Content is required")
    private String content;

    private Boolean isAnswered;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

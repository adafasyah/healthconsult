package com.group3.healthconsult.dto;

import java.time.LocalDateTime;

import com.group3.healthconsult.models.Specialization;
import com.group3.healthconsult.models.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorDto {
    private Long id;
    
    @NotEmpty(message = "User is required")
    private User user;

    @NotEmpty(message = "Specialization is required")
    private Specialization specialization;

    @NotEmpty(message = "Medical License is required")
    private String medicalLicenseId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

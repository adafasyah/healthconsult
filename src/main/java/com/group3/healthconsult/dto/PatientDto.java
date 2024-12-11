package com.group3.healthconsult.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.group3.healthconsult.models.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientDto {
    private Long id;

    @NotEmpty(message = "User is required")
    private User user;

    @NotEmpty(message = "Age is required")
    private Integer age;

    @NotEmpty(message = "Gender is required")
    private String gender;

    @NotEmpty(message = "Date of Birth is required")
    private LocalDate dob;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

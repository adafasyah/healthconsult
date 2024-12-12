package com.group3.healthconsult.services;

import java.util.List;
import java.util.Optional;

import com.group3.healthconsult.dto.ConsultationDto;
import com.group3.healthconsult.models.Consultation;

public interface ConsultationService {
    List<ConsultationDto> getAll(
        String search,
        Long specialization
    );
    List<ConsultationDto> findByPatientId(Long patientId);
    Optional<ConsultationDto> findById(Long id);
    Consultation create(Consultation consultation, String tags);
    Consultation update(ConsultationDto consultation);
    Consultation delete(Long id);
}

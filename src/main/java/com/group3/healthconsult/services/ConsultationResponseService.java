package com.group3.healthconsult.services;

import java.util.List;
import java.util.Optional;

import com.group3.healthconsult.dto.ConsultationResponseDto;
import com.group3.healthconsult.models.ConsultationResponse;
import com.group3.healthconsult.models.User;

public interface ConsultationResponseService {
    List<ConsultationResponseDto> getAll();
    List<ConsultationResponseDto> getResponseByUser(User user);
    Optional<ConsultationResponseDto> findById(Long id);
    ConsultationResponse create(Long consultationId, ConsultationResponse consultationResponse);
    ConsultationResponse update(ConsultationResponseDto consultationResponse);
    ConsultationResponse delete(Long id);
}

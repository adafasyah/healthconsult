package com.group3.healthconsult.services;

import java.util.List;
import java.util.Optional;

import com.group3.healthconsult.dto.ConsultationVoteDto;
import com.group3.healthconsult.models.ConsultationVote;

public interface ConsultationVoteService {
    List<ConsultationVoteDto> getAll();
    Optional<ConsultationVoteDto> findById(Long id);
    Optional<ConsultationVoteDto> findByUserAndConsultation(Long userId, Long consultationId);
    ConsultationVote create(Long consultationId, ConsultationVote consultationVote);
    ConsultationVote update(ConsultationVoteDto consultationVote);
    ConsultationVote delete(Long id);
}

package com.group3.healthconsult.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.ConsultationResponse;
import com.group3.healthconsult.models.User;

import java.util.Optional;
import java.util.List;



public interface ConsultationResponseRepository extends JpaRepository<ConsultationResponse, Long> {
    Optional<ConsultationResponse> findById(Long id);
    List<ConsultationResponse> findByUser(User user);
    List<ConsultationResponse> findByConsultation(Consultation consultation);
    List<ConsultationResponse> findByResponseTo(ConsultationResponse responseTo);
    Long countByConsultation(Consultation consultation);
}

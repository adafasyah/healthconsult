package com.group3.healthconsult.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.ConsultationVote;
import com.group3.healthconsult.models.User;

import java.util.List;
import java.util.Optional;


public interface ConsultationVoteRepository extends JpaRepository<ConsultationVote, Long> {
    Optional<ConsultationVote> findById(Long id);
    List<ConsultationVote> findByUser(User user);
    Optional<ConsultationVote> findByConsultation(Consultation consultation);
}

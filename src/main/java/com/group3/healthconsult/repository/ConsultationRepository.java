package com.group3.healthconsult.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.Specialization;

import java.util.List;
import java.util.Optional;
import com.group3.healthconsult.models.Tag;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Optional<Consultation> findById(Long id);
    List<Consultation> findByPatientId(Long patientId);
    List<Consultation> findByTags(List<Tag> tags);
    List<Consultation> findBySpecialization(Specialization specialization);
    List<Consultation> findByTitle(String title);
    List<Consultation> findByIsResolved(Boolean isResolved);
}

package com.group3.healthconsult.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.healthconsult.core.SecurityUtil;
import com.group3.healthconsult.dto.ConsultationDto;
import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.ConsultationResponse;
import com.group3.healthconsult.models.Doctor;
import com.group3.healthconsult.models.Patient;
import com.group3.healthconsult.models.Specialization;
import com.group3.healthconsult.models.Tag;
import com.group3.healthconsult.models.User;
import com.group3.healthconsult.repository.ConsultationRepository;
import com.group3.healthconsult.repository.ConsultationResponseRepository;
import com.group3.healthconsult.repository.ConsultationVoteRepository;
import com.group3.healthconsult.repository.DoctorRepository;
import com.group3.healthconsult.repository.PatientRepository;
import com.group3.healthconsult.repository.SpecializationRepository;
import com.group3.healthconsult.repository.TagRepository;
import com.group3.healthconsult.repository.UserRepository;
import com.group3.healthconsult.services.ConsultationService;

@Service
public class ConsultationServiceImpl implements ConsultationService {
    private ConsultationRepository consultationRepository;
    private ConsultationResponseRepository consultationResponseRepository;
    private ConsultationVoteRepository consultationVoteRepository;
    private UserRepository userRepository;
    private PatientRepository patientRepository;
    private SpecializationRepository specializationRepository;
    private DoctorRepository doctorRepository;
    private TagRepository tagRepository;

    @Autowired
    public ConsultationServiceImpl(
            ConsultationRepository consultationRepository,
            ConsultationResponseRepository consultationResponseRepository,
            ConsultationVoteRepository consultationVoteRepository,
            UserRepository userRepository,
            PatientRepository patientRepository,
            SpecializationRepository specializationRepository,
            DoctorRepository doctorRepository,
            TagRepository tagRepository) {
        this.consultationRepository = consultationRepository;
        this.consultationResponseRepository = consultationResponseRepository;
        this.consultationVoteRepository = consultationVoteRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.specializationRepository = specializationRepository;
        this.doctorRepository = doctorRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<ConsultationDto> getAll(
            String search,
            Long specialization) {
        List<Consultation> consultations = consultationRepository.findAll();
        return consultations.stream()
                .filter(consultation -> {
                    Boolean isTrue = false;

                    if (search == null && specialization == null) {
                        isTrue = true;
                    } else {
                        if (search != null) {
                            isTrue = consultation.getTitle().contains(search);

                            List<Tag> consultationTags = consultation.getTags();
                            isTrue = isTrue || consultationTags.stream()
                                    .anyMatch(tag -> tag.getName().contains(search));
                        }

                        if (specialization != null) {
                            isTrue = isTrue || consultation.getSpecialization().getId() == specialization;
                        }
                    }

                    return isTrue;
                })
                .map(consultationResponse -> mapToConsultationDto(consultationResponse))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationDto> findByPatientId(Long patientId) {
        List<Consultation> consultations = consultationRepository.findByPatientId(patientId);
        return consultations.stream()
                .map(consultation -> mapToConsultationDto(consultation))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConsultationDto> findById(Long id) {
        Optional<Consultation> consultation = consultationRepository.findById(id);
        return consultation.map(value -> mapToConsultationDto(value));
    }

    @Override
    public Consultation create(
            Consultation consultation,
            String tags) {
        String authenticatedUsername = SecurityUtil.getSessionUser();
        Optional<User> user = userRepository.findByUsername(authenticatedUsername);
        User authenticatedUser = user.orElse(null);

        // doctor cannot create consultation
        if (authenticatedUser.getRole().equals("doctor")) {
            return null;
        }

        String[] tagsList = tags.split(",");
        // find tags in database, if not exist, create new tag
        List<Tag> consultationTags = new ArrayList<>();
        
        for (String tagName : tagsList) {
            Tag tag = tagRepository.findByName(tagName);
            if (tag == null) {
                tag = Tag.builder().name(tagName).build();
                tagRepository.save(tag);
            }
            consultationTags.add(tag);
        }

        Patient patient = patientRepository.findByUser(authenticatedUser);
        consultation.setPatient(patient);
        consultation.setTags(consultationTags);
        consultation.setIsResolved(false);

        return consultationRepository.save(consultation);
    }

    @Override
    public Consultation update(ConsultationDto consultationDto) {
        Consultation consultation = mapToConsultation(consultationDto);
        return consultationRepository.save(consultation);
    }

    @Override
    public Consultation delete(Long id) {
        Optional<Consultation> consultation = consultationRepository.findById(id);
        if (consultation.isPresent()) {
            consultationRepository.deleteById(id);
        }
        return consultation.orElse(null);
    }

    public List<ConsultationResponse> getConsultationResponses(Consultation consultation) {
        if (consultation == null) {
            return null;
        }

        return consultationResponseRepository.findByConsultation(consultation);
    }

    public Long getTotalConsultationResponses(Consultation consultation) {
        if (consultation == null) {
            return null;
        }

        return consultationResponseRepository.countByConsultation(consultation);
    }

    public Long getConsultationUpvotesCount(Consultation consultation) {
        if (consultation == null) {
            return null;
        }

        return consultationVoteRepository.findByConsultation(consultation)
                .stream()
                .filter(consultationVote -> consultationVote.getVote() == 1)
                .count();
    }

    public Long getConsultationDownvotesCount(Consultation consultation) {
        if (consultation == null) {
            return null;
        }

        return consultationVoteRepository.findByConsultation(consultation)
                .stream()
                .filter(consultationVote -> consultationVote.getVote() == -1)
                .count();
    }

    private ConsultationDto mapToConsultationDto(Consultation consultation) {
        ConsultationDto consultationDto = ConsultationDto.builder()
                .id(consultation.getId())
                .patient(consultation.getPatient())
                .consult_to(consultation.getConsult_to())
                .specialization(consultation.getSpecialization())
                .tags(consultation.getTags())
                .isResolved(consultation.getIsResolved())
                .title(consultation.getTitle())
                .content(consultation.getContent())
                .responses(getConsultationResponses(consultation))
                .responsesCount(getTotalConsultationResponses(consultation))
                .upvotesCount(getConsultationUpvotesCount(consultation))
                .downvotesCount(getConsultationDownvotesCount(consultation))
                .createdAt(consultation.getCreatedAt())
                .updatedAt(consultation.getUpdatedAt())
                .build();

        return consultationDto;
    }

    private Consultation mapToConsultation(ConsultationDto consultationDto) {
        Consultation consultation = Consultation.builder()
                .id(consultationDto.getId())
                .patient(consultationDto.getPatient())
                .consult_to(consultationDto.getConsult_to())
                .specialization(consultationDto.getSpecialization())
                .tags(consultationDto.getTags())
                .isResolved(consultationDto.getIsResolved())
                .title(consultationDto.getTitle())
                .content(consultationDto.getContent())
                .createdAt(consultationDto.getCreatedAt())
                .updatedAt(consultationDto.getUpdatedAt())
                .build();

        return consultation;
    }
}

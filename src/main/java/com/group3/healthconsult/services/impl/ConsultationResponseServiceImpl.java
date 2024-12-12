package com.group3.healthconsult.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.healthconsult.core.SecurityUtil;
import com.group3.healthconsult.dto.ConsultationResponseDto;
import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.ConsultationResponse;
import com.group3.healthconsult.models.Doctor;
import com.group3.healthconsult.models.User;
import com.group3.healthconsult.repository.ConsultationRepository;
import com.group3.healthconsult.repository.ConsultationResponseRepository;
import com.group3.healthconsult.repository.DoctorRepository;
import com.group3.healthconsult.repository.UserRepository;
import com.group3.healthconsult.services.ConsultationResponseService;

@Service
public class ConsultationResponseServiceImpl implements ConsultationResponseService {
    private ConsultationResponseRepository consultationResponseRepository;
    private ConsultationRepository consultationRepository;
    private UserRepository userRepository;
    private DoctorRepository doctorRepository;

    @Autowired
    public ConsultationResponseServiceImpl(
        ConsultationResponseRepository consultationResponseRepository,
        ConsultationRepository consultationRepository,
        UserRepository userRepository,
        DoctorRepository doctorRepository
    ) {
        this.consultationResponseRepository = consultationResponseRepository;
        this.consultationRepository = consultationRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<ConsultationResponseDto> getAll() {
        List<ConsultationResponse> consultationResponses = consultationResponseRepository.findAll();
        return consultationResponses.stream()
                .map(consultationResponse -> mapToConsultationResponseDto(consultationResponse))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConsultationResponseDto> findById(Long id) {
        Optional<ConsultationResponse> doctor = consultationResponseRepository.findById(id);
        return doctor.map(value -> mapToConsultationResponseDto(value));
    }

    @Override
    public ConsultationResponse create(Long consultationId, ConsultationResponse consultationResponse) {
        String authenticatedUsername = SecurityUtil.getSessionUser();
        Optional<User> user = userRepository.findByUsername(authenticatedUsername);
        User authenticatedUser = user.orElse(null);

        Consultation consultation = consultationRepository.findById(consultationId).orElse(null);
        Doctor doctor = doctorRepository.findByUser(authenticatedUser).orElse(null);

        consultationResponse.setUser(authenticatedUser);
        consultationResponse.setConsultation(consultation);
        consultationResponse.setDoctorRef(doctor);
        consultationResponse.setIsDoctor(doctor != null);
        consultationResponse.setIsConsultationOwner(consultation.getPatient().getUser().getId().equals(authenticatedUser.getId()));

        if (consultation.getConsult_to() != null && doctor != null && consultation.getConsult_to().getId().equals(doctor.getId())) {
            consultation.setIsResolved(true);
            consultationResponse.setIsAnswered(true);
            consultationRepository.save(consultation);
        } else if (consultationResponse.getIsAnswered() != null && consultationResponse.getIsAnswered().equals(true)) {
            consultation.setIsResolved(true);
            consultationRepository.save(consultation);
        }

        return consultationResponseRepository.save(consultationResponse);
    }

    @Override
    public ConsultationResponse update(ConsultationResponseDto consultationResponseDto) {
        ConsultationResponse consultationResponse = mapToConsultationResponse(consultationResponseDto);
        return consultationResponseRepository.save(consultationResponse);
    }

    @Override
    public ConsultationResponse delete(Long id) {
        Optional<ConsultationResponse> consultationResponse = consultationResponseRepository.findById(id);
        if (consultationResponse.isPresent()) {
            consultationResponseRepository.deleteById(id);
        }
        return consultationResponse.orElse(null);
    }

    @Override
    public List<ConsultationResponseDto> getResponseByUser(User user) {
        List<ConsultationResponse> consultationResponses = consultationResponseRepository.findByUser(user);
        
        return consultationResponses.stream()
                .map(consultationResponse -> mapToConsultationResponseDto(consultationResponse))
                .collect(Collectors.toList());
    }

    private ConsultationResponseDto mapToConsultationResponseDto(ConsultationResponse consultationResponse) {
        ConsultationResponseDto consultationResponseDto = ConsultationResponseDto.builder()
            .id(consultationResponse.getId())
            .consultation(consultationResponse.getConsultation())
            .user(consultationResponse.getUser())
            .responseTo(consultationResponse.getResponseTo())
            .isConsultationOwner(consultationResponse.getIsConsultationOwner())
            .isDoctor(consultationResponse.getIsDoctor())
            .doctorRef(consultationResponse.getDoctorRef())
            .isAnswered(consultationResponse.getIsAnswered())
            .content(consultationResponse.getContent())
            .createdAt(consultationResponse.getCreatedAt())
            .updatedAt(consultationResponse.getUpdatedAt())
            .build();

        return consultationResponseDto;
    }

    private ConsultationResponse mapToConsultationResponse(ConsultationResponseDto consultationResponseDto) {
        ConsultationResponse consultationResponse = ConsultationResponse.builder()
                .id(consultationResponseDto.getId())
                .consultation(consultationResponseDto.getConsultation())
                .user(consultationResponseDto.getUser())
                .responseTo(consultationResponseDto.getResponseTo())
                .isConsultationOwner(consultationResponseDto.getIsConsultationOwner())
                .isDoctor(consultationResponseDto.getIsDoctor())
                .doctorRef(consultationResponseDto.getDoctorRef())
                .isAnswered(consultationResponseDto.getIsAnswered())
                .content(consultationResponseDto.getContent())
                .createdAt(consultationResponseDto.getCreatedAt())
                .updatedAt(consultationResponseDto.getUpdatedAt())
                .build();

        return consultationResponse;
    }
}

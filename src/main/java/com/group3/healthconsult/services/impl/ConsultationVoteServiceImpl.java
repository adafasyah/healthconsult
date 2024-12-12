package com.group3.healthconsult.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.healthconsult.core.SecurityUtil;
import com.group3.healthconsult.dto.ConsultationVoteDto;
import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.ConsultationVote;
import com.group3.healthconsult.models.User;
import com.group3.healthconsult.repository.ConsultationRepository;
import com.group3.healthconsult.repository.ConsultationVoteRepository;
import com.group3.healthconsult.repository.UserRepository;
import com.group3.healthconsult.services.ConsultationVoteService;

@Service
public class ConsultationVoteServiceImpl implements ConsultationVoteService {
    private ConsultationVoteRepository consultationVoteRepository;
    private ConsultationRepository consultationRepository;
    private UserRepository userRepository;

    @Autowired
    public ConsultationVoteServiceImpl(
        ConsultationVoteRepository consultationVoteRepository,
        ConsultationRepository consultationRepository,
        UserRepository userRepository
    ) {
        this.consultationVoteRepository = consultationVoteRepository;
        this.consultationRepository = consultationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ConsultationVoteDto> getAll() {
        List<ConsultationVote> consultationVotes = consultationVoteRepository.findAll();
        return consultationVotes.stream().map(consultationVote -> mapToConsultationVoteDto(consultationVote))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ConsultationVoteDto> findById(Long id) {
        Optional<ConsultationVote> doctor = consultationVoteRepository.findById(id);
        return doctor.map(value -> mapToConsultationVoteDto(value));
    }

    @Override
    public Optional<ConsultationVoteDto> findByUserAndConsultation(Long userId, Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        Optional<ConsultationVote> consultationVote = consultationVoteRepository.findByConsultation(consultation);
        return consultationVote.filter(value -> value.getUser().equals(user)).map(value -> mapToConsultationVoteDto(value));
    }

    @Override
    public ConsultationVote create(Long consultationId, ConsultationVote consultationVote) {
        String authenticatedUsername = SecurityUtil.getSessionUser();
        Optional<User> user = userRepository.findByUsername(authenticatedUsername);
        User authenticatedUser = user.orElse(null);

        Consultation consultation = consultationRepository.findById(consultationId).orElse(null);
        ConsultationVote consultationVoteData = consultationVoteRepository.findByConsultation(consultation).filter(value -> value.getUser().equals(authenticatedUser)).orElse(null);

        if (consultationVoteData != null) {
            consultationVoteData.setVote(consultationVote.getVote());
            return consultationVoteRepository.save(consultationVoteData);            
        }

        consultationVote.setUser(authenticatedUser);
        consultationVote.setConsultation(consultation);
        consultationVote.setVote(consultationVote.getVote());
        return consultationVoteRepository.save(consultationVote);
    }

    @Override
    public ConsultationVote update(ConsultationVoteDto consultationVoteDto) {
        ConsultationVote consultationVote = mapToConsultationVote(consultationVoteDto);
        return consultationVoteRepository.save(consultationVote);
    }

    @Override
    public ConsultationVote delete(Long id) {
        Optional<ConsultationVote> consultationVote = consultationVoteRepository.findById(id);
        if (consultationVote.isPresent()) {
            consultationVoteRepository.deleteById(id);
        }
        return consultationVote.orElse(null);
    }

    private ConsultationVoteDto mapToConsultationVoteDto(ConsultationVote consultationVote) {
        ConsultationVoteDto consultationVoteDto = ConsultationVoteDto.builder()
            .id(consultationVote.getId())
            .consultation(consultationVote.getConsultation())
            .user(consultationVote.getUser())
            .vote(consultationVote.getVote())
            .createdAt(consultationVote.getCreatedAt())
            .updatedAt(consultationVote.getUpdatedAt())
            .build();

        return consultationVoteDto;
    }

    private ConsultationVote mapToConsultationVote(ConsultationVoteDto consultationVoteDto) {
        ConsultationVote consultationVote = ConsultationVote.builder()
                .id(consultationVoteDto.getId())
                .consultation(consultationVoteDto.getConsultation())
                .user(consultationVoteDto.getUser())
                .vote(consultationVoteDto.getVote())
                .createdAt(consultationVoteDto.getCreatedAt())
                .updatedAt(consultationVoteDto.getUpdatedAt())
                .build();

        return consultationVote;
    }
}

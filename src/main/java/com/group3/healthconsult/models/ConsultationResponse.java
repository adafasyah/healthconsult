package com.group3.healthconsult.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "consultation_responses")
public class ConsultationResponse {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultation.id", foreignKey = @ForeignKey(name = "FK_consultation_responses_consultations"))
    private Consultation consultation;

    @ManyToOne
    @JoinColumn(name = "user.id", foreignKey = @ForeignKey(name = "FK_consultation_responses_users"))
    private User user;
    
    @Nullable
    @ManyToOne
    @JoinColumn(name = "response.id", foreignKey = @ForeignKey(name = "FK_consultation_responses_responses"))
    private ConsultationResponse responseTo;

    @Nullable
    private Boolean isDoctor;

    @ManyToOne
    @JoinColumn(name = "doctor.id", foreignKey = @ForeignKey(name = "FK_responses_doctors"))
    private Doctor doctorRef;

    @Nullable
    private Boolean isConsultationOwner;

    @Lob
    private String content;

    private Boolean isAnswered;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

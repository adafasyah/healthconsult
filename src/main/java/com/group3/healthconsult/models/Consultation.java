package com.group3.healthconsult.models;

import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "consultations")
public class Consultation {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient.id", foreignKey = @ForeignKey(name = "FK_consultation_patients"))
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor.id", foreignKey = @ForeignKey(name = "FK_consultation_doctors"))
    private Doctor consult_to;

    @ManyToOne
    @JoinColumn(name = "specialization.id", foreignKey = @ForeignKey(name = "FK_consultation_specializations"))
    private Specialization specialization;

    @ManyToMany
    @JoinTable(
        name = "consultation_tags",
        joinColumns = @JoinColumn(name = "consultation.id"),
        inverseJoinColumns = @JoinColumn(name = "tag.id")
    )
    private List<Tag> tags;

    private String title;
    private Boolean isResolved;

    @Lob
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

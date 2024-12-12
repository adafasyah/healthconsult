package com.group3.healthconsult.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.group3.healthconsult.dto.ConsultationDto;
import com.group3.healthconsult.dto.ConsultationResponseDto;
import com.group3.healthconsult.dto.PatientDto;
import com.group3.healthconsult.services.ConsultationResponseService;
import com.group3.healthconsult.services.ConsultationService;
import com.group3.healthconsult.services.PatientService;

@Controller
public class PatientController {
    private PatientService patientService;
    private ConsultationService consultationService;
    private ConsultationResponseService consultationResponseService;

    @Autowired
    public PatientController(
        PatientService patientService,
        ConsultationService consultationService,
        ConsultationResponseService consultationResponseService
    ) {
        this.patientService = patientService;
        this.consultationService = consultationService;
        this.consultationResponseService = consultationResponseService;
    }

    // GET (List)
    @GetMapping("/patients")
    public String getAllPatients(Model model) {
        List<PatientDto> patients = patientService.getAll();
        model.addAttribute("patients", patients);
        return "redirect:/";
    }

    // GET
    @GetMapping("/patients/{id}")
    public String getPatientById(@PathVariable("id") Long id, Model model) {
        PatientDto patient = patientService.findById(id).orElse(null);
        List<ConsultationResponseDto> consultationResponses = this.consultationResponseService.getResponseByUser(patient.getUser());
        List<ConsultationDto> consultations = this.consultationService.findByPatientId(id);
        model.addAttribute("patient", patient);
        model.addAttribute("responses", consultationResponses);
        model.addAttribute("activities", consultations);
        return "patients/show";
    }
}

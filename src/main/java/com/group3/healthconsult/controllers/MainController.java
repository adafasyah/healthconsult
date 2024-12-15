package com.group3.healthconsult.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.group3.healthconsult.dto.ConsultationDto;
import com.group3.healthconsult.dto.DoctorDto;
import com.group3.healthconsult.dto.SpecializationDto;
import com.group3.healthconsult.models.ConsultationVote;
import com.group3.healthconsult.services.ConsultationService;
import com.group3.healthconsult.services.DoctorService;
import com.group3.healthconsult.services.SpecializationService;

@Controller
public class MainController {
    private ConsultationService consultationService;
    private SpecializationService specializationService;
    private DoctorService doctorService;

    @Autowired
    public MainController(
        ConsultationService consultationService,
        SpecializationService specializationService,
        DoctorService doctorService
    ) {
        this.consultationService = consultationService;
        this.specializationService = specializationService;
        this.doctorService = doctorService;
    }

    @GetMapping("/")
    public String index(
        Model model,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Long specialization
    ) {
        List<ConsultationDto> consultations = consultationService.getAll(search, specialization);
        List<SpecializationDto> specializations = specializationService.getAll();
        List<DoctorDto> doctors = doctorService.getAll(specialization);
        ConsultationVote consultationVote = new ConsultationVote();

        model.addAttribute("consultations", consultations);
        model.addAttribute("specializations", specializations);
        model.addAttribute("doctors", doctors);
        model.addAttribute("vote", consultationVote);
        return "index";
    }

}

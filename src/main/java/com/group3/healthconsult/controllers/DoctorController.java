package com.group3.healthconsult.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.group3.healthconsult.dto.ConsultationResponseDto;
import com.group3.healthconsult.dto.DoctorDto;
import com.group3.healthconsult.models.Doctor;
import com.group3.healthconsult.services.ConsultationResponseService;
import com.group3.healthconsult.services.DoctorService;

import jakarta.validation.Valid;

@Controller
public class DoctorController {
    private DoctorService doctorService;
    private ConsultationResponseService consultationResponseService;

    @Autowired DoctorController(
        DoctorService doctorService,
        ConsultationResponseService consultationResponseService
    ) {
        this.doctorService = doctorService;
        this.consultationResponseService = consultationResponseService;
    }

    // GET (List)
    @GetMapping("/doctors")
    public String getAllDoctors(Model model) {
        List<DoctorDto> doctors = doctorService.getAll(null);
        model.addAttribute("doctors", doctors);
        return "redirect:/";
    }

    // POST
    @GetMapping("/doctors/{id}")
    public String getDoctorById(@PathVariable("id") Long id, Model model) {
        DoctorDto doctor = doctorService.findById(id).orElse(null);
        List<ConsultationResponseDto> consultationResponses = this.consultationResponseService.getResponseByUser(doctor.getUser());
        model.addAttribute("doctor", doctor);
        model.addAttribute("responses", consultationResponses);
        model.addAttribute("activities", consultationResponses.stream().map(response -> { return response.getConsultation(); }).toList());
        return "doctors/show";
    }

    // POST (Create)
    @PostMapping("/doctors")
    public String createNewDoctor(@Valid @ModelAttribute("doctor") Doctor doctor, BindingResult result) {
        if (result.hasErrors()) {
            return "doctors/create";
        }

        doctorService.create(doctor);
        return "redirect:/doctors";
    }
}

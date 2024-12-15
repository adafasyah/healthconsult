package com.group3.healthconsult.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.group3.healthconsult.core.SecurityUtil;
import com.group3.healthconsult.dto.SpecializationDto;
import com.group3.healthconsult.models.Doctor;
import com.group3.healthconsult.models.Patient;
import com.group3.healthconsult.models.User;
import com.group3.healthconsult.services.DoctorService;
import com.group3.healthconsult.services.PatientService;
import com.group3.healthconsult.services.SpecializationService;
import com.group3.healthconsult.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {
    private UserService userService;
    private DoctorService doctorService;
    private PatientService patientService;
    private SpecializationService specializationService;

    public AuthController(
        UserService userService,
        DoctorService doctorService,
        PatientService patientService,
        SpecializationService specializationService
    ) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.specializationService = specializationService;
    }

   @GetMapping("/login")
    public String login() {
        String authenticatedUsername = SecurityUtil.getSessionUser();
        Optional<User> authenticatedUser = userService.findByUsername(authenticatedUsername);

        if (authenticatedUser.isPresent()) {
            return "redirect:/";
        }

        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        List<SpecializationDto> specializations = specializationService.getAll();

        User user = new User();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();

        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", patient);
        model.addAttribute("specializations", specializations);

        return "register";
    }

    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("user") User user,
        @Valid @ModelAttribute("doctor") Doctor doctor,
        @Valid @ModelAttribute("patient") Patient patient,
        BindingResult result,
        Model model
    ) {
        Optional<User> existingUserOptional = userService.findByUsername(user.getUsername());
        
        if (existingUserOptional.isPresent()) {
            result.rejectValue("username", "error.user", "Username is already taken");
            return "redirect:/register?fail=true";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "redirect:/register?fail=true";
        }

        userService.create(user);

        if (user.getRole().equals("doctor")) {
            doctor.setUser(user);
            doctorService.create(doctor);
        } else {
            patient.setUser(user);
            patientService.create(patient);
        }
    
        return "redirect:/login?registerSuccess=true";
    }
}

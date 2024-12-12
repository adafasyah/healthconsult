package com.group3.healthconsult.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.group3.healthconsult.core.SecurityUtil;
import com.group3.healthconsult.dto.ConsultationDto;
import com.group3.healthconsult.dto.DoctorDto;
import com.group3.healthconsult.dto.SpecializationDto;
import com.group3.healthconsult.models.Consultation;
import com.group3.healthconsult.models.ConsultationResponse;
import com.group3.healthconsult.models.ConsultationVote;
import com.group3.healthconsult.models.Doctor;
import com.group3.healthconsult.models.Tag;
import com.group3.healthconsult.models.User;
import com.group3.healthconsult.services.ConsultationResponseService;
import com.group3.healthconsult.services.ConsultationService;
import com.group3.healthconsult.services.ConsultationVoteService;
import com.group3.healthconsult.services.DoctorService;
import com.group3.healthconsult.services.SpecializationService;
import com.group3.healthconsult.services.UserService;

import jakarta.validation.Valid;

@Controller
public class ConsultationController {
    private ConsultationService consultationService;
    private ConsultationResponseService consultationResponseService;
    private ConsultationVoteService consultationVoteService;
    private DoctorService doctorService;
    private SpecializationService specializationService;
    private UserService userService;

    @Autowired
    public ConsultationController(
            ConsultationService consultationService,
            ConsultationResponseService consultationResponseService,
            ConsultationVoteService consultationVoteService,
            DoctorService doctorService,
            SpecializationService specializationService,
            UserService userService) {
        this.consultationService = consultationService;
        this.consultationResponseService = consultationResponseService;
        this.consultationVoteService = consultationVoteService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.userService = userService;
    }

    // GET (List)
    @GetMapping("/consultations")
    public String getAllConsultations(
            Model model,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long specialization) {
        List<ConsultationDto> consultations = consultationService.getAll(search, specialization);
        model.addAttribute("consultations", consultations);
        return "redirect:/";
    }

    @GetMapping("/consultations/new")
    public String createNewConsultation(
            Model model) {
        List<DoctorDto> doctors = doctorService.getAll(null);
        List<SpecializationDto> specializations = specializationService.getAll();

        String authenticatedUsername = SecurityUtil.getSessionUser();
        Optional<User> user = userService.findByUsername(authenticatedUsername);
        Consultation consultation = new Consultation();

        if (user.isPresent()) {
            User authenticatedUser = user.get();

            if (authenticatedUser.getRole().equals("doctor")) {
                model.addAttribute("isDoctor", true);
            } else {
                model.addAttribute("doctors", doctors);
                model.addAttribute("specializations", specializations);
            }
        } else {
            model.addAttribute("isDoctor", false);
            model.addAttribute("doctors", null);
            model.addAttribute("specializations", null);
        }

        model.addAttribute("consultation", consultation);
        model.addAttribute("tags", new Tag());
        return "consultations/index";
    }

    // POST
    @GetMapping("/consultations/{id}")
    public String getConsultationById(@PathVariable("id") Long id, Model model) {
        ConsultationDto consultation = consultationService.findById(id).orElse(null);

        String authenticatedUsername = SecurityUtil.getSessionUser();
        Optional<User> user = userService.findByUsername(authenticatedUsername);
        ConsultationResponse consultationResponse = new ConsultationResponse();

        model.addAttribute("consultation", consultation);

        if (user.isPresent()) {
            User authenticatedUser = user.get();
            Doctor doctor = doctorService.findByUser(authenticatedUser).orElse(null);

            model.addAttribute("authenticatedUser", authenticatedUser);
            model.addAttribute("authenticatedDoctor", doctor);

            if (doctor != null) {
                consultationResponse.setIsAnswered(
                        consultation.getConsult_to() != null && consultation.getConsult_to().getId() == doctor.getId());
            }
        } else {
            model.addAttribute("authenticatedUser", null);
            model.addAttribute("authenticatedDoctor", null);
        }

        model.addAttribute("response", consultationResponse);
        return "consultations/show";
    }

    // POST (Create)
    @PostMapping("/consultations")
    public String createNewConsultation(
            @Valid @ModelAttribute("consultation") Consultation consultation,
            @ModelAttribute("tags") Tag tags,
            BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/consultations/new";
        }

        consultationService.create(consultation, tags.getName());
        return "redirect:/";
    }

    @PostMapping("/consultations/{id}/response")
    public String postResponse(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("response") ConsultationResponse consultationResponse,
            BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/consultations/" + id;
        }

        consultationResponseService.create(id, consultationResponse);
        return "redirect:/consultations/" + id;
    }

    @PostMapping("/consultations/{id}/upvote")
    public String upvoteConsultation(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("vote") ConsultationVote consultationVote,
            BindingResult result) {
        String authenticatedUsername = SecurityUtil.getSessionUser();
        Optional<User> user = userService.findByUsername(authenticatedUsername);
        User authenticatedUser = user.orElse(null);

        if (result.hasErrors()) {
            return "redirect:/consultations/" + id;
        }

        if (authenticatedUser == null) {
            return "redirect:/login";
        }

        consultationVote.setVote(1);
        consultationVoteService.create(id, consultationVote);
        return "redirect:/";
    }

    @PostMapping("/consultations/{id}/downvote")
    public String downvoteConsultation(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("vote") ConsultationVote consultationVote,
            BindingResult result) {
        String authenticatedUsername = SecurityUtil.getSessionUser();
        Optional<User> user = userService.findByUsername(authenticatedUsername);
        User authenticatedUser = user.orElse(null);

        if (result.hasErrors()) {
            return "redirect:/consultations/" + id;
        }

        if (authenticatedUser == null) {
            return "redirect:/login";
        }

        consultationVote.setVote(-1);
        consultationVoteService.create(id, consultationVote);
        return "redirect:/";
    }
}

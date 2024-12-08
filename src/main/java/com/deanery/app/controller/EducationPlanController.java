package com.deanery.app.controller;


import com.deanery.app.aspect.ActiveUserCheck;
import com.deanery.app.aspect.CustomSecured;
import com.deanery.app.dto.EducationPlanDto;
import com.deanery.app.dto.IndividualDto;
import com.deanery.app.dto.ViewEducationPlanDto;
import com.deanery.app.dto.ViewIndividualDto;
import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.model.Individual;
import com.deanery.app.model.User;
import com.deanery.app.model.WorkPlan;
import com.deanery.app.service.EducationPlanService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/education")
@Slf4j
public class EducationPlanController {

    private final EducationPlanService educationPlanService;

    @GetMapping
    @ActiveUserCheck
    public String getEduPlans(Model model){
        model.addAttribute("educations",
                educationPlanService.findAllEduPlansList().stream()
                        .map(ViewEducationPlanDto::new)
                        .toList());
        return "education";
    }

    @GetMapping(value = { "/view/{id}"})
    @ActiveUserCheck
    public String viewEducation(@PathVariable UUID id, Model model){
        if (id != null) {
            model.addAttribute("educationId", id);
            model.addAttribute("education", new EducationPlanDto(educationPlanService.findEducationPlan(id)));
            model.addAttribute("workPlans", educationPlanService.findWorkPlans(id));
        }
        else{
            model.addAttribute("errors", "Doesnt find Education plan");
            return "education";
        }
        return "education-view";
    }

    @PostMapping("/setEducationPlan/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String setIndividualCode(@PathVariable UUID id, Model model) {
        try {
            educationPlanService.createWorkPlans(id);
            return "redirect:/education/view/" + id;
        }
        catch (ValidationException e){
            model.addAttribute("educationId", id);
            model.addAttribute("education", new EducationPlanDto(educationPlanService.findEducationPlan(id)));
            model.addAttribute("errors", e.getMessage());
            return "education-view";
        }
    }

    @GetMapping(value = {"/edit", "/view/edit/{id}"})
    @ActiveUserCheck
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    public String createEducationPLan(@PathVariable(required = false) UUID id,
                                   Model model) {
        if (id == null) {
            model.addAttribute("educationDto", new EducationPlanDto());
        } else{
            final EducationPlan educationPlan = educationPlanService.findEducationPlan(id);
            model.addAttribute("educationId", id);
            model.addAttribute("educationDto", new EducationPlanDto(educationPlan));
        }
        return "education-edit";
    }

    @PostMapping(value = {"/edit/", "/edit/{id}"})
    @ActiveUserCheck
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    public String saveMaster(@PathVariable(required = false) UUID id,
                             @ModelAttribute("educationDto") @Valid EducationPlanDto educationPlanDto,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            if (id == null) {
                model.addAttribute("educationId", "");
            } else{
                model.addAttribute("educationId", id);
            }
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "education-edit";
        }
        try {
            if ( id == null) {
                educationPlanService.create(educationPlanDto);
            } else {
                educationPlanService.update(id, educationPlanDto);
            }
            return "redirect:/education";
        } catch (ValidationException e) {
            if (id == null) {
                model.addAttribute("educationId", "");
            } else{
                model.addAttribute("educationId", id);
            }
            model.addAttribute("errors", e.getMessage());
            return "education-edit";
        }
    }

    @PostMapping("/delete/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String deleteIndividual(@PathVariable UUID id) {
        educationPlanService.delete(id);
        return "redirect:/education";
    }
}

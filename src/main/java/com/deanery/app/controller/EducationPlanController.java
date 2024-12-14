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
import com.deanery.app.repository.WorkPlanRepository;
import com.deanery.app.service.EducationPlanService;
import com.deanery.app.service.IndividualService;
import com.deanery.app.service.LogInfoService;
import com.deanery.app.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/education")
@Slf4j
public class EducationPlanController {

    private final EducationPlanService educationPlanService;
    private final LogInfoService logInfoService;
    private final UserService userService;
    private final IndividualService individualService;

    @GetMapping
    @ActiveUserCheck
    public String getEduPlans(Model model){
        model.addAttribute("educations",
                educationPlanService.findAllEduPlansList().stream()
                        .map(ViewEducationPlanDto::new)
                        .toList());
        model.addAttribute("userRole", getCurrentUser().getRole());
        return "education";
    }

    @GetMapping(value = { "/view/{id}"})
    @ActiveUserCheck
    public String viewEducation(@PathVariable UUID id, Model model){
        if (id != null) {
            model.addAttribute("educationId", id);
            model.addAttribute("education", new EducationPlanDto(educationPlanService.findEducationPlan(id)));
            model.addAttribute("workPlans", educationPlanService.findWorkPlans(id));
            model.addAttribute("logs", logInfoService.getLogs(id));
            model.addAttribute("iwp", educationPlanService.findIWP(id));
            model.addAttribute("userRolee", getCurrentUser().getRole());
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

    @PostMapping("/setNextCourse/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String setNextCourse(@PathVariable UUID id, Model model) {
        try {
            individualService.setNextCourse(id, getCurrentUser(), educationPlanService.findEducationPlan(id));
            return "redirect:/education/view/" + id;
        }
        catch (ValidationException e){
            model.addAttribute("educationId", id);
            model.addAttribute("education", new EducationPlanDto(educationPlanService.findEducationPlan(id)));
            model.addAttribute("workPlans", educationPlanService.findWorkPlans(id));
            model.addAttribute("logs", logInfoService.getLogs(id));
            model.addAttribute("iwp", educationPlanService.findIWP(id));
            model.addAttribute("errors", e.getMessage());
            return "education-view";
        }
    }

    @PostMapping("/startCourse/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String startCourse(@PathVariable UUID id, Model model) {
        try {
            educationPlanService.updateStatus(id);
            return "redirect:/education/view/" + id;
        }
        catch (ValidationException e){
            model.addAttribute("educationId", id);
            model.addAttribute("education", new EducationPlanDto(educationPlanService.findEducationPlan(id)));
            model.addAttribute("workPlans", educationPlanService.findWorkPlans(id));
            model.addAttribute("logs", logInfoService.getLogs(id));
            model.addAttribute("iwp", educationPlanService.findIWP(id));
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
                EducationPlan eduPlan = educationPlanService.findEducationPlanByNum(educationPlanDto.hashCode());
                educationPlanService.createWorkPlans(eduPlan.getId());
                logInfoService.create(eduPlan.getId(), "Добавление учебного плана", getCurrentUser());
                logInfoService.create(eduPlan.getId(), "Добавление рабочих планов", getCurrentUser());
                return "redirect:/education/view/" + eduPlan.getId();
            } else {

                EducationPlan eduPlan = educationPlanService.findEducationPlan(id);
                StringBuilder str = new StringBuilder();
                str.append("Обновление учебного плана.");
                if(eduPlan.getEdu_form() != educationPlanDto.getEdu_form())
                    str.append(" Изменение формы обучения с ").append(eduPlan.getEdu_form()).append(" на ").append(educationPlanDto.getEdu_form()).append(".");
                if(eduPlan.getEdu_type() != educationPlanDto.getEdu_type())
                    str.append(" Изменение типа обучения с ").append(eduPlan.getEdu_type()).append(" на ").append(educationPlanDto.getEdu_type()).append(".");
                if(eduPlan.getEdu_qual() != educationPlanDto.getEdu_qual())
                    str.append(" Изменение квалификации обучения с ").append(eduPlan.getEdu_qual()).append(" на ").append(educationPlanDto.getEdu_qual()).append(".");
                if(!Objects.equals(eduPlan.getFullName(), educationPlanDto.getFullName()))
                    str.append(" Изменение названия обучения обучения с ").append(eduPlan.getFullName()).append(" на ").append(educationPlanDto.getFullName()).append(".");

                educationPlanService.update(id, educationPlanDto);
                educationPlanService.updateWorkPlans(id);

                logInfoService.create(id, str.toString(), getCurrentUser());
                logInfoService.create(id, "Обновление рабочих планов.", getCurrentUser());
            }
            return "redirect:/education/view/" + id;
        } catch (ValidationException e) {
            if (id == null) {
                model.addAttribute("educationId", "");
            } else{
                final EducationPlan educationPlan = educationPlanService.findEducationPlan(id);
                model.addAttribute("educationId", id);
                model.addAttribute("educationDto", new EducationPlanDto(educationPlan));
            }
            model.addAttribute("errors", e.getMessage());
            return "education-edit";
        }
    }

    @PostMapping("/delete/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String delete(@PathVariable UUID id) {
        educationPlanService.delete(id);
        logInfoService.create(id, "Изменение статуса учебного плана на недействительный.", getCurrentUser());
        return "redirect:/education";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof DefaultOAuth2User userDetails) {
                String email = userDetails.getAttribute("email");
                return userService.findByEmail(email);
            } else if (principal instanceof UserDetails userDetails) {
                return userService.findByEmail(userDetails.getUsername());
            }
        }
        return null;
    }
}

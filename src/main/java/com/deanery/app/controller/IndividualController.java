package com.deanery.app.controller;

import com.deanery.app.aspect.ActiveUserCheck;
import com.deanery.app.aspect.CustomSecured;
import com.deanery.app.dto.IndividualDto;
import com.deanery.app.dto.IndividualPlanDto;
import com.deanery.app.dto.ViewIndividualDto;
import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.model.Enums.WorkPlanStatus;
import com.deanery.app.model.Individual;
import com.deanery.app.model.IndividualWorkPlan;
import com.deanery.app.model.User;
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

import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/individual")
@Slf4j
public class IndividualController {
    private final IndividualService individualService;
    private final UserService userService;
    private final LogInfoService logInfoService;
    private final EducationPlanService educationPlanService;

    @GetMapping(value = {"/edit", "/view/edit/{id}"})
    @ActiveUserCheck
    public String createIndividual(@PathVariable(required = false) UUID id,
                       Model model) {
        if (id == null) {
            model.addAttribute("individualDto", new IndividualDto());
        } else{
            final Individual individual = individualService.findIndividual(id);
            if(individual.getIndividualCode() == null || getCurrentUser().getRole() == UserRole.ADMIN){
                model.addAttribute("individualId", id);
                model.addAttribute("individualDto", new IndividualDto(individual));
            }
            else {
                model.addAttribute("individualId", id);
                model.addAttribute("individual", new IndividualDto(individualService.findIndividual(id)));
                model.addAttribute("userRole", getCurrentUser().getRole().toString());
                model.addAttribute("errors", "Недостаточно прав.");
                return "individual-view";
            }
        }
        return "individual-edit";
    }

    @GetMapping(value = { "/view/{id}"})
    @ActiveUserCheck
    public String viewIndividual(@PathVariable(required = false) UUID id, Model model){
        if (id != null) {
            User user = getCurrentUser();
            final IndividualDto ind = new IndividualDto(individualService.findIndividual(id));
            model.addAttribute("individualId", id);
            model.addAttribute("individual", ind);
            model.addAttribute("userRole", user.getRole().toString());
            model.addAttribute("logs", logInfoService.getLogs(id));
            model.addAttribute("indPlans", individualService.findAllIndividualWP(id));
            if(userService.findByEmail(ind.getEmail()) != null)
                model.addAttribute("user", userService.findByEmail(ind.getEmail()));
        }
        else{
            model.addAttribute("errors", "Doesnt find Individual");
            return "individuals";
        }
        return "individual-view";
    }

    @PostMapping(value = {"/edit", "/edit/{id}"})
    @ActiveUserCheck
    public String saveMaster(@PathVariable(required = false) UUID id,
                             @ModelAttribute("individualDto") @Valid IndividualDto individualDto,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            if (id == null) {
                model.addAttribute("individualId", "");
            } else{
                model.addAttribute("individualId", id);
            }
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "individual-edit";
        }
        try {
            if ( id == null) {
                individualService.create(individualDto);
                Individual ind = individualService.findBySnils(individualDto.getSnils());
                logInfoService.create(ind.getId(), "Добавление физического лица", getCurrentUser());
                return "redirect:/individual/view/" + ind.getId();
            } else {
                Individual ind = individualService.findBySnils(individualDto.getSnils());
                StringBuilder str = new StringBuilder();
                str.append("Обновление физ. лица.");

                if(!Objects.equals(ind.getFirst_name(), individualDto.getFirst_name()))
                    str.append(" Изменение имени с ").append(ind.getFirst_name()).append(" на ").append(individualDto.getFirst_name()).append(".");
                if(!Objects.equals(ind.getSecond_name(), individualDto.getSecond_name()))
                    str.append(" Изменение фамилиии с ").append(ind.getSecond_name()).append(" на ").append(individualDto.getSecond_name()).append(".");
                if(!Objects.equals(ind.getPatronymic(), individualDto.getPatronymic()))
                    str.append(" Изменение отчества с ").append(ind.getPatronymic()).append(" на ").append(individualDto.getPatronymic()).append(".");
                if(!Objects.equals(ind.getEmail(), individualDto.getEmail()))
                    str.append(" Изменение почты с ").append(ind.getEmail()).append(" на ").append(individualDto.getEmail()).append(".");
                if(!Objects.equals(ind.getRegistration(), individualDto.getRegistration()))
                    str.append(" Изменение адреса регистрации с ").append(ind.getRegistration()).append(" на ").append(individualDto.getRegistration()).append(".");
                if(!Objects.equals(ind.getActualAddress(), individualDto.getActualAddress()))
                    str.append(" Изменение актуального адреса с ").append(ind.getActualAddress()).append(" на ").append(individualDto.getActualAddress()).append(".");
                if(!Objects.equals(ind.getSnils(), individualDto.getSnils()))
                    str.append(" Изменение снилса с ").append(ind.getSnils()).append(" на ").append(individualDto.getSnils()).append(".");
                if(!Objects.equals(ind.getBirthplace(), individualDto.getBirthplace()))
                    str.append(" Изменение места рождения с ").append(ind.getBirthplace()).append(" на ").append(individualDto.getBirthplace()).append(".");
                if(!ind.getBirthday().equals(individualDto.getBirthday()))
                    str.append(" Изменение даты рождения с ").append(ind.getBirthday()).append(" на ").append(individualDto.getBirthday()).append(".");
                if(!Objects.equals(ind.getInn(), individualDto.getInn()))
                    str.append(" Изменение места регистрации с ").append(ind.getInn()).append(" на ").append(individualDto.getInn()).append(".");
                individualService.updateIndividual(id, individualDto);
                logInfoService.create(ind.getId(), str.toString(), getCurrentUser());
            }
            return "redirect:/individual/view/" + id;
        } catch (ValidationException e) {
            if (id == null) {
                model.addAttribute("individualId", "");
            } else{
                model.addAttribute("individualId", id);
            }
            model.addAttribute("errors", e.getMessage());
            return "individual-edit";
        }
    }

    @PostMapping("/delete/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String deleteIndividual(@PathVariable UUID id) {
        individualService.deleteIndividual(id);
        logInfoService.create(id, "Изменение статуса физ. лица. на недействительный", getCurrentUser());
        return "redirect:/individual";
    }

    @PostMapping("/setIndividualCode/{id}")
    @CustomSecured(role= {UserRole.AsString.USER, UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String setIndividualCode(@PathVariable UUID id) {
        individualService.createPersonalCode(id);
        logInfoService.create(id, "Утверждение физ. лица.", getCurrentUser());
        return "redirect:/individual/view/" + id;
    }

    @GetMapping
    @ActiveUserCheck
    public String getIndividuals(Model model){
        model.addAttribute("individuals",
                individualService.findAllIndividualsList().stream()
                        .map(ViewIndividualDto::new)
                        .toList());
        return "individuals";
    }

    @GetMapping("/addWorkPlan/{id}")
    @ActiveUserCheck
    public String createBind(@PathVariable UUID id, Model model){
        if(id == null){
            throw new ValidationException("Individual with this id doesnt exist");
        }
        IndividualPlanDto ipd = new IndividualPlanDto();
        ipd.setIndividual(individualService.findIndividual(id));
        model.addAttribute("indPlanDto", ipd);
        model.addAttribute("individualId", id);
        model.addAttribute("individual", individualService.findIndividual(id));
        return "individualPlan";
    }


    @PostMapping("/addWorkPlan/{id}")
    @ActiveUserCheck
    public String createBind(@PathVariable UUID id,
            @ModelAttribute("indWpDto") @Valid IndividualPlanDto individualPlanDto,
            BindingResult bindingResult,
            Model model){
        try {
            individualService.createBind(individualPlanDto);
            logInfoService.create(individualPlanDto.getIndividual().getId(), "Зачисление на " + individualPlanDto.getWorkPlan().getName() + "-"+individualPlanDto.getWorkPlan().getCourse().toString(), getCurrentUser());
            return "redirect:/individual/view/"+individualPlanDto.getIndividual().getId();
        }
        catch (ValidationException e){
            IndividualPlanDto ipd = new IndividualPlanDto();
            ipd.setIndividual(individualService.findIndividual(id));
            model.addAttribute("indPlanDto", ipd);
            model.addAttribute("individualId", id);
            model.addAttribute("individual", individualService.findIndividual(id));
            model.addAttribute("errors", e.getMessage());
            return "individualPlan";
        }

    }

    @PostMapping("/changeBind/{id}")
    @ActiveUserCheck
    public String updateStatusBind(@PathVariable UUID id,
                                   @RequestParam(value ="status", required = false) String status){
        IndividualWorkPlan iwp = individualService.findBind(id);

        switch (status) {
            case "expelled":
                individualService.updateStatusBind(id, WorkPlanStatus.EXPELLED);
                logInfoService.create(iwp.getIndividual().getId(), "Отчислен", getCurrentUser());
                break;
            case "academ":
                individualService.updateStatusBind(id, WorkPlanStatus.ACADEM);
                logInfoService.create(iwp.getIndividual().getId(), "Переведен в академ", getCurrentUser());
                break;
            case "transferout":
                individualService.updateStatusBind(id, WorkPlanStatus.TRANSFERREDOUT);
                logInfoService.create(iwp.getIndividual().getId(), "Переведен в другое учебное заведение", getCurrentUser());
                break;
        }
            return "redirect:/individual/view/"+iwp.getIndividual().getId();
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

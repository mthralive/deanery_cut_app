package com.deanery.app.controller;

import com.deanery.app.aspect.ActiveUserCheck;
import com.deanery.app.aspect.CustomSecured;
import com.deanery.app.dto.IndividualDto;
import com.deanery.app.dto.ViewIndividualDto;
import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.model.Individual;
import com.deanery.app.model.User;
import com.deanery.app.service.IndividualService;
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

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/individual")
@Slf4j
public class IndividualController {
    private final IndividualService individualService;
    private final UserService userService;

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
            model.addAttribute("individualId", id);
            model.addAttribute("individual", new IndividualDto(individualService.findIndividual(id)));
            model.addAttribute("userRole", user.getRole().toString());
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
            } else {
                individualService.updateIndividual(id, individualDto);
            }
            return "redirect:/individual";
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
        return "redirect:/individual";
    }

    @PostMapping("/setIndividualCode/{id}")
    @CustomSecured(role= {UserRole.AsString.USER, UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String setIndividualCode(@PathVariable UUID id) {
        individualService.createPersonalCode(id);
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

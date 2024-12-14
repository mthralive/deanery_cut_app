package com.deanery.app.controller;

import com.deanery.app.aspect.ActiveUserCheck;
import com.deanery.app.aspect.CustomSecured;
import com.deanery.app.dto.IndividualDto;
import com.deanery.app.dto.RequestDto;
import com.deanery.app.dto.ViewIndividualDto;
import com.deanery.app.dto.ViewRequestDto;
import com.deanery.app.model.Enums.RequestStatus;
import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.model.Individual;
import com.deanery.app.model.Request;
import com.deanery.app.model.User;
import com.deanery.app.service.IndividualService;
import com.deanery.app.service.RequestService;
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
@RequestMapping("/request")
@Slf4j
public class RequestController {
    private final RequestService requestService;
    private final UserService userService;
    private final IndividualService individualService;

    @GetMapping
    @ActiveUserCheck
    public String getRequests(Model model){
        final User u = getCurrentUser();
        if(u.getRole() == UserRole.USER){
            model.addAttribute("requests",
                    requestService.findAllRequestByUser(u.getId()).stream()
                            .map(ViewRequestDto::new)
                            .toList());
        }
        else {
            model.addAttribute("requests",
                    requestService.findAllRequest().stream()
                            .map(ViewRequestDto::new)
                            .toList());
        }
        return "request";
    }

    @GetMapping("/filtered")
    @ActiveUserCheck
    public String getRequestsFiltered(@RequestParam(value ="status", required = false) RequestStatus status, Model model){
        if(status != null) {
            model.addAttribute("requests",
                    requestService.findAllRequestByStatus(status).stream()
                            .map(ViewRequestDto::new)
                            .toList());
        }
        else{
            model.addAttribute("requests",
                    requestService.findAllRequest().stream()
                            .map(ViewRequestDto::new)
                            .toList());
        }
        return "request";
    }

    @GetMapping("/create/{id}")
    @ActiveUserCheck
    public String createRequest(@PathVariable UUID id, Model model){
        final Individual ind = individualService.findIndividual(id);
        RequestDto rd = new RequestDto();
        rd.setUser(getCurrentUser());
        rd.setIndividual(ind);
        model.addAttribute("userRole", getCurrentUser().getRole().toString());
        model.addAttribute("requestDto", rd);
        return "request-create";
    }

    @GetMapping("/view/{id}")
    @ActiveUserCheck
    public String viewRequest(@PathVariable UUID id, Model model){
        final Request r = requestService.findRequest(id);
        model.addAttribute("userRole", getCurrentUser().getRole().toString());
        model.addAttribute("request", r);
        model.addAttribute("requestId", r.getId());
        return "request-view";
    }
    @PostMapping("/create")
    @ActiveUserCheck
    public String saveRequest(@ModelAttribute("requestDto") @Valid RequestDto requestDto,
                              Model model){
        try {
            requestService.create(requestDto);
            Request r = requestService.find(requestDto.getUser().getId(), requestDto.getIndividual().getId());
            return "redirect:/request/view/" + r.getId();
        } catch (ValidationException e) {
            final Individual ind = individualService.findIndividual(requestDto.getIndividual().getId());
            RequestDto rd = new RequestDto();
            rd.setUser(getCurrentUser());
            rd.setIndividual(ind);
            model.addAttribute("userRole", getCurrentUser().getRole().toString());
            model.addAttribute("requestDto", new RequestDto());
            return "request-create";
        }
    }

    @PostMapping("/delete/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String close(@PathVariable UUID id) {
        requestService.cancelRequest(id);
        return "redirect:/request";
    }

    @PostMapping("/stat/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String stat(@PathVariable UUID id) {
        requestService.updateStatus(id);
        return "redirect:/request";
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

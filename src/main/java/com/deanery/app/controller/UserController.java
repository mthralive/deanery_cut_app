package com.deanery.app.controller;

import com.deanery.app.aspect.ActiveUserCheck;
import com.deanery.app.aspect.CustomSecured;
import com.deanery.app.dto.ViewIndividualDto;
import com.deanery.app.dto.user.UserDto;
import com.deanery.app.dto.user.ViewUserDto;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.model.Individual;
import com.deanery.app.model.User;
import com.deanery.app.service.IndividualService;
import com.deanery.app.service.LogInfoService;
import com.deanery.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
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
@Secured({UserRole.AsString.USER, UserRole.AsString.ADMIN})
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final IndividualService individualService;
    private final LogInfoService logInfoService;

    @GetMapping("/edit/{id}")
    @ActiveUserCheck
    public String edit(@PathVariable(required = true) UUID id, Model model) {

        if (id != null) {
            UserDto userDto = new UserDto(userService.find(id));
            model.addAttribute("userId", id);
            userDto.setPassword("");
            model.addAttribute("userDto", userDto);
        }
        return "user-edit";
    }

    @GetMapping("/create/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String createUser(@PathVariable(required = true) UUID id,
                             Model model){
        if(id != null){
            final Individual individual = individualService.findIndividual(id);
            if(individual != null){
                UserDto userDto = new UserDto(individual.getEmail(), "", UserRole.USER, individual);
                model.addAttribute("userDto", userDto);
                model.addAttribute("individual", individual);
            }
        }
        return "user-edit";
    }


    @PostMapping(value={"/edit", "/edit/{id}"})
    @ActiveUserCheck
    public String update(@PathVariable(required = false) UUID id,
                         @ModelAttribute("userDto") @Valid UserDto userDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "user-edit";
        }
        if (id == null) {
            userService.create(userDto);
            logInfoService.create(userDto.getIndividual().getId(), "Создание пользователя", getCurrentUser());
        }
        else{
            User user = userService.find(id);
            userService.update(user.getId(), userDto);
            logInfoService.create(userDto.getIndividual().getId(), "Обновление пользователя", getCurrentUser());
        }
        return "redirect:/user";
    }

    @PostMapping("/delete/{id}")
    @CustomSecured(role= {UserRole.AsString.ADMIN})
    @ActiveUserCheck
    public String deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        logInfoService.create(id, "Изменение статуса пользователя на недействительный", getCurrentUser());
        return "redirect:/user";
    }

    @GetMapping
    @ActiveUserCheck
    public String getUsers(Model model){
        model.addAttribute("users",
                userService.findAllUsersList().stream()
                        .map(ViewUserDto::new)
                        .toList());
        return "users";
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

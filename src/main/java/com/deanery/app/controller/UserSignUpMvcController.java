package com.deanery.app.controller;

import com.deanery.app.dto.user.UserSignUpDto;
import com.deanery.app.model.User;
import com.deanery.app.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(UserSignUpMvcController.SIGNUP_URL)
@RequiredArgsConstructor
@Slf4j
public class UserSignUpMvcController {
    public static final String SIGNUP_URL = "/signup";
    private final UserService userService;

    @GetMapping
    public String showSignupForm(Model model) {
        model.addAttribute("userDto", new UserSignUpDto());
        return "signup";
    }

//    @PostMapping
//    public String signup(@ModelAttribute("userDto") @Valid UserSignUpDto userSignupDto,
//                         BindingResult bindingResult,
//                         Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("errors", bindingResult.getAllErrors());
//            return "signup";
//        }
//        try {
//            final User user = userService.create(userSignupDto);
//            return "redirect:/email?created=" + user.getEmail();
//        } catch (ValidationException e) {
//            model.addAttribute("errors", e.getMessage());
//            return "signup";
//        }
//    }
}
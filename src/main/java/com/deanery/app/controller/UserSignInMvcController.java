package com.deanery.app.controller;

import com.deanery.app.dto.user.UserSignInDto;
import com.deanery.app.model.User;
import com.deanery.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping(UserSignInMvcController.LOGIN_URL)
@RequiredArgsConstructor
@Slf4j
public class UserSignInMvcController {
    public static final String LOGIN_URL = "/login";
    private final UserService userService;

    @GetMapping("/otpVerification")
    public String otpSent(UserSignInDto userSignInDto, Model model) {
        model.addAttribute("otpValue", userSignInDto);
        return "otp-screen";
    }

    @PostMapping("/otpVerification")
    public String otpVerification(@ModelAttribute("otpValue") UserSignInDto userLoginDTO,
                                  HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User users = userService.findByEmail(user.getUsername());
        if (Objects.equals(users.getOtp(), userLoginDTO.getOtp())) {
            users.setIsActive(true);
            userService.callRepositorySave(users);
            return "redirect:/dashboard";
        } else {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, securityContext.getAuthentication());
            return "redirect:/login/otpVerification?error";
        }
    }
}
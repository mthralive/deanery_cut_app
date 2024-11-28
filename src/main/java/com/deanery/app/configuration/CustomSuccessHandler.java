package com.deanery.app.configuration;

import com.deanery.app.dto.user.UserSignUpDto;
import com.deanery.app.model.User;
import com.deanery.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectUrl;

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            redirectUrl = handleOAuth2User((DefaultOAuth2User) authentication.getPrincipal());
        } else {
            redirectUrl = handleUserDetails((UserDetails) authentication.getPrincipal());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String handleOAuth2User(DefaultOAuth2User userDetails) {
        String email = userDetails.getAttribute("email") != null ?
                userDetails.getAttribute("email") : userDetails.getAttribute("login") + "@gmail.com";
        User user = userService.findByEmail(email);
        if (user != null) {
            user.setIsActive(true);
            userService.callRepositorySave(user);
        }
        return "/dashboard";
    }

    private String handleUserDetails(UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        user.setOtp(userService.generateOtp(user));
        userService.callRepositorySave(user);
        return "/login/otpVerification";
    }

    private String generateRandomPhoneNumber() {
        Random random = new Random();
        String regionCode = String.format("%03d", random.nextInt(1000));
        String firstThreeDigits = String.format("%03d", random.nextInt(1000));
        String lastFourDigits = String.format("%04d", random.nextInt(10000));
        return String.format("+7(%s)%s-%s-%s", regionCode, firstThreeDigits, lastFourDigits.substring(0, 2),
                lastFourDigits.substring(2));
    }
}

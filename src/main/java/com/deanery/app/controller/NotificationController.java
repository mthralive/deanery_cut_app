package com.deanery.app.controller;

import com.deanery.app.aspect.ActiveUserCheck;
import com.deanery.app.model.Notification;
import com.deanery.app.model.User;
import com.deanery.app.service.NotificationService;
import com.deanery.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {
    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping
    @ActiveUserCheck
    public String userNotifications(Pageable pageable, Model model) {

        Page<Notification> notifications = notificationService.findByUserOrderByIsChecked(getCurrentUser(), PageRequest.of(pageable.getPageNumber(), 5));
        model.addAttribute("notifications", notifications.stream().toList());

        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", notifications.getTotalPages());
        return "notification";
    }

    @PostMapping("/all-checked")
    @ActiveUserCheck
    public String allChecked(Model model) {
        notificationService.checkedNotificationsWithoutPage(userService.findByPrincipal());
        model.addAttribute("hasUnreadNotifications", false);
        return "redirect:/notifications";
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
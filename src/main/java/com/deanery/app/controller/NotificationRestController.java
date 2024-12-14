package com.deanery.app.controller;

import com.deanery.app.aspect.ActiveUserCheck;
import com.deanery.app.model.Notification;
import com.deanery.app.model.User;
import com.deanery.app.repository.NotificationRepository;
import com.deanery.app.service.NotificationService;
import com.deanery.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationRestController {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    @PostMapping("/{id}")
    @ActiveUserCheck
    public Boolean checkNotification(Model model, @PathVariable UUID id) {
        User user = userService.findByPrincipal();
        Notification n = notificationService.findByUUID(id);
        n.setIsChecked(true);
        Boolean flag = notificationRepository.countByUserAndIsCheckedFalse(user) != 1;
        model.addAttribute("hasUnreadNotifications", flag);
        notificationService.save(n);
        return flag;
    }

    @PostMapping("/delete")
    @ActiveUserCheck
    public Boolean deleteNotifications(Model model, @RequestBody List<UUID> request) {
        Boolean hasUnreadNotifications = notificationService.delete(getCurrentUser(), request);
        model.addAttribute("hasUnreadNotifications", hasUnreadNotifications);
        return hasUnreadNotifications;
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

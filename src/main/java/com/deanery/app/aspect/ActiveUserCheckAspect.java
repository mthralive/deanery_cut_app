package com.deanery.app.aspect;

import com.deanery.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ActiveUserCheckAspect {
    private final UserService userService;

    @Around("@annotation(activeUserCheck)")
    public Object checkActiveUser(ProceedingJoinPoint joinPoint,
                                  ActiveUserCheck activeUserCheck) throws Throwable {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            boolean isActive = userService.findByEmail(userDetails.getUsername()).getIsActive();
            if (!isActive) {
                throw new DisabledException("User account is disabled");
            }
        }
        return joinPoint.proceed();
    }
}

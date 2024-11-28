package com.deanery.app.service;

import com.deanery.app.dto.user.UserDto;
import com.deanery.app.dto.user.UserSignUpDto;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.model.Individual;
import com.deanery.app.model.User;
import com.deanery.app.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends DefaultOAuth2UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    @Value("{spring.mail.username}")
    private String emailFrom;

    @Transactional(readOnly = true)
    public Page<User> findAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page - 1, size, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public User findByPrincipal() {
        User user = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOAuth2User userDetails) {
            user = findByEmail(userDetails.getAttribute("email"));
        } else if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            user = findByEmail(userDetails.getUsername());
        }
        if (user == null) {
            throw new UserNotFoundException(null);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findOneByEmailIgnoreCase(email);
    }

    @Transactional
    public User create(UserDto userDto) {
        final User user = findByEmail(userDto.getEmail());
        if (user != null) {
            throw new ValidationException(String.format("User '%s' already exists", user.getEmail()));
        }
        UserRole role;
        if (userRepository.findAll().isEmpty()) {
            role = UserRole.ADMIN;
        } else {
            role = UserRole.USER;
        }
        final User newUser = new User(null, userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
                userDto.getIndividual() ,role, 0,
                false);
        return userRepository.save(newUser);
    }

    @Transactional
    public User callRepositorySave(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User find(UUID id) {
        final Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User update(UUID id, UserDto userDto) {
        final User currentUser = find(id);
        final User sameUser = findByEmail(userDto.getEmail());
        if (sameUser != null && !Objects.equals(sameUser.getId(), currentUser.getId())) {
            throw new ValidationException(String.format("User '%s' already exists", userDto.getEmail()));
        }
        currentUser.setEmail(userDto.getEmail());
        currentUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        currentUser.setRole(userDto.getUserRole());
        currentUser.setIndividual(userDto.getIndividual());
        return userRepository.save(currentUser);
    }

    @Transactional
    public User delete(UUID id) {
        final User user = find(id);
        userRepository.deleteById(id);
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsersList() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User userEntity = findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(), userEntity.getPassword(), Collections.singleton(userEntity.getRole()));
    }

    public Integer generateOtp(User user) {
        user.setIsActive(false);
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailFrom);
        msg.setTo(user.getEmail());
        msg.setText("Hello \n\n" + "Your Login OTP: " + randomPIN + ". Please Verify. \n\n");

        javaMailSender.send(msg);
        return randomPIN;
    }
}

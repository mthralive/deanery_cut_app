package com.deanery.app.repository;

import com.deanery.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findOneByEmailIgnoreCase(String email);
    User findUserById(UUID id);
}

package com.adrian.school.management.system.auth;

import com.adrian.school.management.system.entity.Student;
import com.adrian.school.management.system.entity.User;
import com.adrian.school.management.system.repository.StudentRepository;
import com.adrian.school.management.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {

    private final StudentRepository studentRepository;

    public Authentication getCurrentLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUserEmail() {
        return getCurrentLoggedUser().getName();
    }

    public Student getStudent() {
        String studentEmail = getUserEmail();

        return studentRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with email " + studentEmail + " not found."));
    }
}

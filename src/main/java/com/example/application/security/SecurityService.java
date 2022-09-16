package com.example.application.security;

import com.example.application.model.User;
import com.example.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final SessionRegistry sessionRegistry;
    private final UserDetailsService userService;

    public List<User> getAllAuthenticatedUsers() {
        return sessionRegistry.getAllPrincipals().stream()
                .map(principal -> (User) principal)
                .collect(Collectors.toList());
    }

    public Optional<User> getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof Jwt) {
            String username = ((Jwt) principal).getSubject();
            return Optional.of((User) userService.loadUserByUsername(username));
        }
        // Anonymous or no authentication.
        return Optional.empty();
    }

    //TODO Delete this method after setting up R2DBC auth
    public String getAuthenticatedUserUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof Jwt) {
            return  ((Jwt) principal).getSubject();
        }

        return StringUtils.EMPTY;
    }
}

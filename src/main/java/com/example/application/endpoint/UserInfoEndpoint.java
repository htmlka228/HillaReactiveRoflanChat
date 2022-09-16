package com.example.application.endpoint;

import com.example.application.model.User;
import com.example.application.security.SecurityService;
import com.example.application.service.UserService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Endpoint
@RequiredArgsConstructor
@AnonymousAllowed
@Slf4j
public class UserInfoEndpoint {
    private final SecurityService securityService;

    public @Nonnull User getUserInfo() {
        return securityService.getAuthenticatedUser().orElse(null);
    }
}

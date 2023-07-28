package com.foodmarket.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("CustomAuthenticationProvider authentication: {}", authentication);
        UserDetails userDetails = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken token) {
            userDetails = authenticationService.authenticate(token);
        } else if (authentication instanceof PreAuthenticatedAuthenticationToken token) {
            userDetails = authenticationService.authenticate(token);
        }

        if (userDetails == null) {
            return null;
        }

        log.info("Authentication successful for user {}", userDetails.getUsername());
        return UsernamePasswordAuthenticationToken.authenticated(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class)
                || authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }

}

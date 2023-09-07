package com.foodmarket.auditing;

import com.foodmarket.model.entity.UserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomSecurityAuditorAware implements AuditorAware<UserEntity> {

    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        new SimpleGrantedAuthority(ROLE_ANONYMOUS);
        return authentication
                .filter(Authentication::isAuthenticated)
                .filter(auth -> !auth.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ANONYMOUS)))
                .map(auth -> (UserEntity) auth.getPrincipal());
    }

}

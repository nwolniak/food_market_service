package com.foodmarket.auditing;

import com.foodmarket.model.entity.UserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomSecurityAuditorAware implements AuditorAware<UserEntity> {

    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        return authentication
                .filter(Authentication::isAuthenticated)
                .map(auth -> (UserEntity) auth.getPrincipal());
    }

}

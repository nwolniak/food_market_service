package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.RegistrationDto;
import com.foodmarket.model.dto.UserDto;
import com.foodmarket.model.entity.UserEntity;
import com.foodmarket.model.mapping.AuthMapper;
import com.foodmarket.repository.UserRepository;
import com.foodmarket.security.AuthenticationService;
import com.foodmarket.security.CookieAuthenticationFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper mapper;

    public UserDto register(RegistrationDto registrationDto) {
        UserEntity userEntity = mapper.registrationToUser(registrationDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity saved = userRepository.save(userEntity);
        return mapper.userToDto(saved);
    }

    public UserDto login(SecurityContext context, HttpServletResponse response) {
        Authentication authentication = context.getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName())
                .orElseThrow();
        Cookie cookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, authenticationService.createToken(userEntity));
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).toSeconds());

        response.addCookie(cookie);
        return mapper.userToDto(userEntity);
    }

    public UserDto getUser(long id) {
        return userRepository.findById(id)
                .map(mapper::userToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with %s id not found in users repository.", id)));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::userToDto)
                .toList();
    }

}

package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.LoginDto;
import com.foodmarket.model.dto.RegistrationDto;
import com.foodmarket.model.dto.UserDto;
import com.foodmarket.model.entity.UserEntity;
import com.foodmarket.model.mapping.AuthMapper;
import com.foodmarket.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final AuthenticationManager authenticationManager;
    public final AuthMapper mapper;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public UserDto register(RegistrationDto registrationDto) {
        UserEntity userEntity = mapper.registrationToUser(registrationDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity saved = userRepository.save(userEntity);
        return mapper.userToDto(saved);
    }

    public void login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        var token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginDto.username(), loginDto.password());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
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

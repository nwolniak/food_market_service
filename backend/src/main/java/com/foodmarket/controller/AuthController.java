package com.foodmarket.controller;

import com.foodmarket.model.dto.LoginDto;
import com.foodmarket.model.dto.RegistrationDto;
import com.foodmarket.model.dto.UserDto;
import com.foodmarket.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food-market")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("users/{id}")
    public UserDto getUser(@PathVariable long id) {
        return authService.getUser(id);
    }

    @GetMapping("users")
    public List<UserDto> getUsers() {
        return authService.getUsers();
    }

    @PostMapping("registration")
    public UserDto register(@RequestBody RegistrationDto registrationDto) {
        return authService.register(registrationDto);
    }

    @PostMapping("login")
    public UserDto login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        return authService.login(loginDto, request, response);
    }

}

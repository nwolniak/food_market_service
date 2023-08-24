package com.foodmarket.controller;

import com.foodmarket.model.dto.RegistrationDto;
import com.foodmarket.model.dto.UserDto;
import com.foodmarket.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food-market")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("registration")
    public UserDto register(@RequestBody RegistrationDto registrationDto) {
        return userService.register(registrationDto);
    }

    @PostMapping("login")
    public UserDto login(@CurrentSecurityContext SecurityContext context, HttpServletResponse response) {
        return userService.login(context, response);
    }

    @GetMapping("users/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @GetMapping("users")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PatchMapping("password/change")
    public UserDto changePassword(@RequestBody String password, HttpServletResponse response, @CurrentSecurityContext SecurityContext context) {
        return userService.changePassword(password, response, context);
    }

}

package com.kglsys.web.controller;

import com.kglsys.application.service.AuthService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.request.LoginRequest;
import com.kglsys.dto.request.UserRegistrationRequest;
import com.kglsys.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        authService.registerUser(registrationRequest);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.loginUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }
}
package com.kglsys.application.service;

import com.kglsys.dto.request.LoginRequest;
import com.kglsys.dto.request.UserRegistrationRequest;
import com.kglsys.dto.response.LoginResponse;

public interface AuthService {
    void registerUser(UserRegistrationRequest registrationRequest);
    LoginResponse loginUser(LoginRequest loginRequest);
}
package com.kglsys.application.service.user;

import com.kglsys.dto.user.request.LoginRequest;
import com.kglsys.dto.user.request.UserRegistrationRequest;
import com.kglsys.dto.user.response.LoginResponse;

public interface AuthService {
    void registerUser(UserRegistrationRequest registrationRequest);
    LoginResponse loginUser(LoginRequest loginRequest);
}
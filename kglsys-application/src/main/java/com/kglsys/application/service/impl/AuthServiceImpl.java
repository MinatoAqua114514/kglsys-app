package com.kglsys.application.service.impl;

import com.kglsys.application.service.AuthService;
import com.kglsys.common.exception.DuplicateResourceException;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.entity.Role;
import com.kglsys.domain.entity.User;
import com.kglsys.dto.request.LoginRequest;
import com.kglsys.dto.request.UserRegistrationRequest;
import com.kglsys.dto.response.LoginResponse;
import com.kglsys.infra.repository.RoleRepository;
import com.kglsys.infra.repository.UserRepository;
import com.kglsys.infra.details.CustomUserDetails;
import com.kglsys.infra.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public void registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("用户名已存在: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("邮箱已注册: " + request.getEmail());
        }

        Role userRole = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("角色不存在: " + request.getRoleName()));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(userRole))
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return LoginResponse.builder()
                .accessToken(jwt)
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }
}
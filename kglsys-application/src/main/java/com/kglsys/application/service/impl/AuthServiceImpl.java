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
import com.kglsys.infra.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证授权服务的实现类。
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtProvider;

    @Override
    @Transactional
    public void registerUser(UserRegistrationRequest request) {
        // 1. 检查用户名和邮箱是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("用户名已存在: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("邮箱已注册: " + request.getEmail());
        }

        // 2. 查找指定的角色
        Role userRole = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("角色不存在: " + request.getRoleName()));

        // 3. 创建 User 实体
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(userRole))
                .build();

        // 4. 持久化用户
        userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        // 1. 【核心】将认证委托给 Spring Security 的 AuthenticationManager
        // 它会使用我们配置的 DaoAuthenticationProvider 来调用 CustomUserDetailsService 和 PasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. 认证成功后，将认证信息存入 SecurityContextHolder，以便后续的请求可以识别用户身份
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 使用 JwtProvider 生成 JWT
        String jwt = jwtProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 4. 从 UserDetails 中提取角色信息
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 5. 构建并返回 LoginResponse
        return LoginResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }
}
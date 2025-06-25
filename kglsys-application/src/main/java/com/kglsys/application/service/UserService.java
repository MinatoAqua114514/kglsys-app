package com.kglsys.application.service;

import com.kglsys.application.mapper.UserMapper;
import com.kglsys.api.request.RegisterRequest;
import com.kglsys.api.request.LoginRequest;
import com.kglsys.api.response.RegisterResponse;
import com.kglsys.domain.entity.RoleEntity;
import com.kglsys.domain.entity.UserEntity;
import com.kglsys.domain.repository.RoleRepository;
import com.kglsys.domain.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    private static final Set<String> ALLOWED_ROLES = Set.of("student", "teacher");

    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequest, String encodedPassword) {
        if (!ALLOWED_ROLES.contains(registerRequest.getRole())) {
            throw new IllegalArgumentException("角色不合法，只能是 student 或 teacher");
        }
        RoleEntity role = roleRepository.findByName(registerRequest.getRole())
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        UserEntity user = new UserEntity();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setRoles(Collections.singleton(role));

        UserEntity savedUser = userRepository.save(user);
        RegisterResponse registerResponse = userMapper.toRegisterResponse(savedUser);
        registerResponse.setRoles(user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()));
        return registerResponse;
    }

    public Optional<UserEntity> loginUser(LoginRequest loginRequest) {
        // 1. 查找用户
        Optional<UserEntity> userOpt = userRepository.findByUsernameIgnoreCase(loginRequest.getUsername());
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        UserEntity user = userOpt.get();

        // 2. 校验密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return Optional.empty();
        }

        // 3. 校验角色
        if (!ALLOWED_ROLES.contains(loginRequest.getRole())) {
            throw new IllegalArgumentException("角色不合法，只能是 student 或 teacher");
        }
        boolean hasRole = user.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase(loginRequest.getRole()));
        if (!hasRole) {
            throw new IllegalArgumentException("用户没有该角色");
        }

        return Optional.of(user);
    }
}
package com.kglsys.application.service.impl.user;

import com.kglsys.application.mapper.user.UserAdminMapper;
import com.kglsys.application.service.user.AdminUserService;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.user.Role;
import com.kglsys.domain.user.User;
import com.kglsys.dto.user.request.UserRoleUpdateRequest;
import com.kglsys.dto.user.request.UserStatusUpdateRequest;
import com.kglsys.dto.user.response.UserAdminViewVo;
import com.kglsys.infra.repository.user.RoleRepository;
import com.kglsys.infra.repository.user.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserAdminMapper userAdminMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserAdminViewVo> listUsers(String username, String email, Pageable pageable) {
        // 1. 构建动态查询条件
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(username)) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }
            if (StringUtils.hasText(email)) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // 2. 执行查询
        Page<User> userPage = userRepository.findAll(spec, pageable);

        // 3. 映射结果
        return userPage.map(userAdminMapper::toAdminVo);
    }

    @Override
    @Transactional
    public UserAdminViewVo updateUserRole(Long userId, UserRoleUpdateRequest request) {
        User user = findUserById(userId);
        Role newRole = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("角色不存在: " + request.getRoleName()));

        // [FIX] Use a mutable Set (HashSet) instead of an immutable one.
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(newRole);
        user.setRoles(newRoles);

        User updatedUser = userRepository.save(user);

        return userAdminMapper.toAdminVo(updatedUser);
    }

    @Override
    @Transactional
    public UserAdminViewVo updateUserStatus(Long userId, UserStatusUpdateRequest request) {
        User user = findUserById(userId);
        user.setEnabled(request.getEnabled());
        User updatedUser = userRepository.save(user);

        return userAdminMapper.toAdminVo(updatedUser);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户未找到，ID: " + userId));
    }
}
package com.kglsys.application.service;

import com.kglsys.domain.entity.base.UserEntity;
import com.kglsys.domain.repository.base.UserRepository;
import com.kglsys.application.custom.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional; // <--- This is the CORRECT import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service to load user-specific data.
 * It is required by Spring Security for authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Your JPA repository for the User entity

    /**
     * Locates the user based on the username. The readOnly = true flag is a performance
     * optimization, telling the persistence provider (like Hibernate) that no changes
     * will be made to the entities, so it can skip dirty checking.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never {@code null})
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    // This annotation now correctly references the Spring version
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user from the database by username or email
        UserEntity user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username));

        // 收集用户的所有权限（包括角色和具体操作权限）
        Set<GrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach(role -> {
            // 1.添加角色本身作为权限（带ROLE_前缀）
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            // 2.添加该角色下的所有具体操作权限
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        // Create a CustomUserDetails object from the User entity
        return CustomUserDetails.create(user);
    }

    /**
     * This method is used by the JWT filter.
     * It loads a user by their ID.
     *
     * @param id The ID of the user to load.
     * @return UserDetails object.
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return CustomUserDetails.create(user);
    }
}
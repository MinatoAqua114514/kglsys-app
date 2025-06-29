package com.kglsys.infra.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kglsys.domain.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String username;
    @JsonIgnore
    private final String password;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id,
                             String username, String email, String password,
                             boolean enabled, boolean accountNonExpired,
                             boolean accountNonLocked, boolean credentialsNonExpired,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = authorities;
    }

    public static CustomUserDetails create(User user) {
        // 1. 获取所有角色的名称，并添加 "ROLE_" 前缀
        List<GrantedAuthority> roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        // 2. 获取所有角色下的所有权限名称
        List<GrantedAuthority> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream()) // 将多个角色的权限集合扁平化为一个流
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

        // 3. 合并角色和权限
        List<GrantedAuthority> authorities = Stream.concat(roles.stream(), permissions.stream())
                .collect(Collectors.toList());

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                authorities // 使用合并后的权限列表
        );
    }

    // UserDetails interface methods...
    // Getters are handled by Lombok, other methods are implemented above
}
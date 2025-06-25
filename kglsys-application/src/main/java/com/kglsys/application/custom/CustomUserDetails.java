package com.kglsys.application.custom;

import com.kglsys.domain.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A custom UserDetails implementation that provides core user information.
 * This is the object that Spring Security will use for authentication and authorization.
 */
@Data
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    @JsonIgnore // Prevent password from being serialized
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Factory method to create a CustomUserDetails instance from a User entity.
     *
     * @param user The User entity.
     * @return A new CustomUserDetails instance.
     */
    public static CustomUserDetails create(UserEntity user) {
        // Assuming your User entity has a getRoles() method that returns a Set<Role>
        // And your Role entity has a getName() method that returns the role name (e.g., "ROLE_USER")
        // 角色名也作为权限
        List<GrantedAuthority> roleAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        // 权限名
        List<GrantedAuthority> permissionAuthorities = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

        // 合并
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(roleAuthorities);
        authorities.addAll(permissionAuthorities);

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Or implement your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or implement your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or implement your logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Or implement your logic
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
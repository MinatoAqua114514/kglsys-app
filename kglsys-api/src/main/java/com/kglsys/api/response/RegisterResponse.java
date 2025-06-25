package com.kglsys.api.response;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterResponse {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private Set<String> roles;
}

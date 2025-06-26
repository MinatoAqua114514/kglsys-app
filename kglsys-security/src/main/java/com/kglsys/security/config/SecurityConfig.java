package com.kglsys.security.config;

import com.kglsys.common.util.JwtAccessDeniedHandler;
import com.kglsys.common.util.JwtAuthenticationEntryPoint;
import com.kglsys.security.filter.JwtAuthenticationFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity( // Enables method-level security annotations like @PreAuthorize
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig {

    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Resource
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Resource
    JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection since we are using stateless JWT authentication
                .csrf(AbstractHttpConfigurer::disable)
                // Configure exception handling for authentication errors
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                // Configure session management to be stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure authorization rules for HTTP requests
                .authorizeHttpRequests(auth -> auth
                        // Permit all requests to authentication endpoints
                        .requestMatchers("/api/auth/**","/api/test/**", "/api/assessment/**").permitAll()
                        // Require authentication for any other request
                        .anyRequest().authenticated()
                );

        // Add the custom JWT filter before the standard UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
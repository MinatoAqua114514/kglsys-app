package com.kglsys.security.config;

import com.kglsys.security.jwt.JwtAuthenticationFilter;
import com.kglsys.security.service.CustomUserDetailsService;
import com.kglsys.security.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 的核心配置类。
 */
@Configuration
@EnableWebSecurity // 启用 Spring Security 的 Web 安全功能
@EnableMethodSecurity(prePostEnabled = true) // 启用方法级别的权限控制（如 @PreAuthorize）
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 自定义的 JWT 认证过滤器
    private final CustomUserDetailsService userDetailsService; // 自定义用户信息服务类
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean
    public PasswordEncoder passwordEncoder() {
        // 定义密码加密器，使用 BCrypt 加密算法
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置认证提供者 (AuthenticationProvider)。
     * 这是 Spring Security 认证逻辑的核心部分。
     * @return DaoAuthenticationProvider 实例
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 使用构造函数注入方式创建 DaoAuthenticationProvider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        // 设置密码加密器
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // 获取全局认证管理器，用于处理认证请求（如登录）
        return config.getAuthenticationManager();
    }

    /**
     * 配置安全过滤器链 (SecurityFilterChain)，定义所有 HTTP 请求的安全策略。
     * @param http HttpSecurity 配置对象
     * @return 配置好的 SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF 保护，因为我们使用的是无状态 JWT 认证机制
                .csrf(AbstractHttpConfigurer::disable)
                // 2. 设置会话管理策略为“无状态”，Spring Security 将不创建/使用 HttpSession
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 【新增】配置异常处理
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                // 3. 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 4. 对以 /api/auth/ 开头的请求路径全部放行（通常是登录、注册等接口）
                        .requestMatchers("/api/auth/**").permitAll()
                        // 5. 其他所有请求都要求认证
                        .anyRequest().authenticated()
                )
                // 6. 设置认证提供器，交给 Spring Security 使用我们定义的认证逻辑
                .authenticationProvider(authenticationProvider())
                // 7. 将 JWT 认证过滤器添加到 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 8. 构建并返回配置完成的 SecurityFilterChain 实例
        return http.build();
    }
}
package com.kglsys.security.jwt;

import com.kglsys.infra.jwt.JwtTokenGenerator;
import com.kglsys.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenGenerator jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. 从 HTTP 请求的 Authorization 头部获取 JWT（如果存在）
            String jwt = getJwtFromRequest(request);

            // 2. 如果 token 非空且合法，继续处理
            if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                // 3. 从 JWT 中提取用户名
                String username = jwtProvider.getUsernameFromToken(jwt);

                // 4. 通过用户名加载用户详细信息（如权限）
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 5. 构造认证对象，包含用户、凭证（此处为 null）和权限
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // 6. 设置认证的附加信息（如 IP 地址、session ID）
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. 将认证信息设置到 SecurityContext 上下文，完成认证过程
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // 8. 处理异常（如 token 无效、解析失败等），打印日志但不中断请求流程
            logger.error("Could not set user authentication in security context", ex);
        }

        // 9. 继续执行过滤器链中的下一个过滤器
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // 从请求头中获取名为 Authorization 的值
        String bearerToken = request.getHeader("Authorization");
        // 检查该值是否存在且以 "Bearer " 开头
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // 返回去掉 "Bearer " 前缀的 JWT 字符串
            return bearerToken.substring(7);
        }
        // 若不符合条件，返回 null
        return null;
    }
}
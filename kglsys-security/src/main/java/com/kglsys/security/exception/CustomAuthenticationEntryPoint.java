package com.kglsys.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kglsys.common.responses.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义认证入口点。
 * 当一个未经认证的用户尝试访问受保护的资源时，Spring Security会调用此类的 commence 方法。
 * 它的核心职责是生成一个标准的、JSON格式的 401 Unauthorized 响应。
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        logger.warn("Unauthorized access attempt to [{} {}]: {}",
                request.getMethod(), request.getRequestURI(), authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Object> apiResponse = ApiResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "认证失败：需要有效的凭证才能访问此资源。"
        );

        // 使用 ObjectMapper 将 ApiResponse 对象序列化为 JSON 字符串并写入响应体
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
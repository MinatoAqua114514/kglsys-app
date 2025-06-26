package com.kglsys.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class handles errors that occur when an unauthenticated user tries to access a protected resource.
 * It will send a 401 Unauthorized response.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * This method is invoked when a user tries to access a secured REST resource without
     * supplying any credentials. We should just send a 401 Unauthorized response because
     * there is no 'login page' to redirect to.
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        logger.warn("Responding with unauthorized error. Message - {}", authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse<?> apiResponse = ApiResponse.error(401, "需要认证才能访问此资源");

        // 使用ObjectMapper将对象转换为JSON字符串并写入响应
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));

    }
}
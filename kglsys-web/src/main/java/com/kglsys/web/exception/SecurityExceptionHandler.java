package com.kglsys.web.exception;

import com.kglsys.common.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 安全认证异常处理器
 * 专门处理 Spring Security 相关的认证和授权异常
 *
 * @author your-name
 * @since 1.0.0
 */
@RestControllerAdvice
@Order(1) // 高优先级，优先处理安全相关异常
public class SecurityExceptionHandler extends BaseExceptionHandler {

    /**
     * 处理认证失败异常的基类
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {

        logWarn("认证失败", request, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "认证失败，请重新登录"));
    }

    /**
     * 处理用户名或密码错误异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {

        logWarn("用户认证失败", request, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "用户名或密码错误"));
    }

    /**
     * 处理账户被禁用异常
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleDisabledException(
            DisabledException ex, HttpServletRequest request) {

        logWarn("账户被禁用", request, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "账户已被禁用，请联系管理员"));
    }

    /**
     * 处理账户被锁定异常
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse<?>> handleLockedException(
            LockedException ex, HttpServletRequest request) {

        logWarn("账户被锁定", request, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "账户已被锁定，请联系管理员"));
    }

    /**
     * 处理访问权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {

        logWarn("权限不足", request,
                String.format("用户: %s, 原因: %s", getCurrentUserName(request), ex.getMessage()));

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "访问被拒绝，您没有访问该资源的权限"));
    }

    /*
    处理自定义业务权限拒绝异常
     * 注意：这里假设你有自定义的 AuthorizationDeniedException
     * 如果没有可以注释掉或删除
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorizationDeniedException(
            AuthorizationDeniedException ex, HttpServletRequest request) {

        logWarn("业务权限拒绝", request,
                String.format("用户: %s, 原因: %s", getCurrentUserName(request), ex.getMessage()));

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "操作被拒绝，您的角色权限不足"));
    }
    */
}
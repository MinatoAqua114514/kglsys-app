package com.kglsys.common.exception;

import com.kglsys.common.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器。
 * 使用 @RestControllerAdvice 注解，使其能拦截整个应用中抛出的异常，
 * 并将它们转换为统一的、结构化的 ApiResponse 格式返回给客户端。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理由 @Valid 注解触发的参数校验失败异常。
     * @param ex MethodArgumentNotValidException 异常实例，包含所有字段的校验错误信息。
     * @return 返回 400 Bad Request 状态码，响应体中包含所有校验失败的字段和错误信息。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "参数校验失败", errors));
    }

    /**
     * 处理资源未找到异常。
     * @param ex ResourceNotFoundException 自定义异常实例。
     * @return 返回 404 Not Found 状态码和错误信息。
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * 处理资源重复或冲突异常。
     * @param ex DuplicateResourceException 自定义异常实例。
     * @return 返回 409 Conflict 状态码和错误信息。
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        logger.warn("Duplicate resource: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    /**
     * 处理违反业务规则的异常。
     * @param ex BusinessRuleException 自定义异常实例。
     * @return 返回 400 Bad Request 状态码和错误信息。
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessRuleException(BusinessRuleException ex) {
        logger.warn("Business rule violation: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    /**
     * 处理 Spring Security 认证失败异常 (401 Unauthorized)。
     * 通常在用户未登录或 Token 无效时触发。
     * @param ex AuthenticationException Spring Security 认证异常。
     * @return 返回 401 Unauthorized 状态码和认证失败信息。
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "认证失败: " + ex.getMessage()));
    }

    /**
     * 【新增】处理 Spring Security 权限不足异常 (403 Forbidden)。
     * 当已认证用户尝试访问其不具备权限的资源时触发。
     * @param ex AccessDeniedException Spring Security 权限异常。
     * @return 返回 403 Forbidden 状态码和权限不足信息。
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN.value(), "权限不足，禁止访问"));
    }

    /**
     * 【新增】处理非法状态异常。
     * 通常表示程序在不期望的状态下被调用。
     * @param ex IllegalStateException 异常实例。
     * @return 返回 500 Internal Server Error 状态码。
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalStateException(IllegalStateException ex) {
        logger.error("Illegal state detected", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部状态错误"));
    }

    // 捕获请求错误 API 的情况
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", "接口不存在：" + ex.getRequestURL());
        response.put("path", ex.getRequestURL());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 全局兜底异常处理器，捕获所有其他未被特定处理器处理的异常。
     * @param ex Exception 任何未捕获的异常。
     * @return 返回 500 Internal Server Error 状态码和通用错误信息。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        logger.error("An unexpected error occurred", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误，请联系管理员"));
    }
}
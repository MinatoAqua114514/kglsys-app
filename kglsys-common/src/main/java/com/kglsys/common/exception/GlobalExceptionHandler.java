package com.kglsys.common.exception;

import com.kglsys.common.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器，拦截应用程序中抛出的各种异常并统一返回结构化的响应。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验失败异常（例如 @Valid 注解验证失败）。
     * @param ex MethodArgumentNotValidException 异常对象，包含所有校验失败的字段信息。
     * @return 返回 400 状态码以及字段错误信息。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // 遍历所有字段错误，将字段名和错误信息提取出来放入 Map
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // 返回结构化的错误响应，状态码 400（Bad Request）
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors));
    }

    /**
     * 处理资源重复异常（如尝试创建已存在的记录）。
     * @param ex DuplicateResourceException 自定义异常，携带冲突信息。
     * @return 返回 409 状态码以及错误描述。
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    /**
     * 处理资源未找到异常（如访问不存在的记录）。
     * @param ex ResourceNotFoundException 自定义异常，携带错误描述。
     * @return 返回 404 状态码。
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * 处理 Spring Security 认证失败的异常。
     * @param ex AuthenticationException Spring Security 抛出的认证异常。
     * @return 返回 401 状态码。
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "认证失败: " + ex.getMessage()));
    }

    /**
     * 捕获所有其他未显式处理的异常，作为兜底处理。
     * @param ex Exception 异常对象。
     * @return 返回 500 状态码，提示服务器内部错误。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        // 实际应用中可以记录日志：log.error("An unexpected error occurred", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误"));
    }

    /**
     * 处理违反业务规则的异常，如不符合角色逻辑、非法状态等。
     * @param ex BusinessRuleException 异常对象
     * @return 返回 400 状态码和异常消息
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessRuleException(BusinessRuleException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
}
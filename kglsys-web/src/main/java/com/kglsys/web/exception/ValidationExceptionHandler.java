package com.kglsys.web.exception;

import com.kglsys.common.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数校验异常处理器
 * 专门处理各种参数校验相关的异常
 *
 * @author your-name
 * @since 1.0.0
 */
@RestControllerAdvice
@Order(1) // 高优先级，优先处理参数校验异常
public class ValidationExceptionHandler extends BaseExceptionHandler {

    /**
     * 处理 @Valid 注解校验参数失败的异常（RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        // 收集所有字段错误信息
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());

        String errorMessage = errors.isEmpty() ? "参数校验失败" : String.join("; ", errors);

        logWarn("请求参数校验失败", request, errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage));
    }

    /**
     * 处理 @Valid 注解校验参数失败的异常（Form Data）
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<?>> handleBindException(
            BindException ex, HttpServletRequest request) {

        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());

        String errorMessage = errors.isEmpty() ? "参数绑定失败" : String.join("; ", errors);

        logWarn("表单参数绑定失败", request, errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage));
    }

    /**
     * 处理单个参数校验失败的异常（如 @RequestParam 配合 @Valid 使用）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        logWarn("参数约束校验失败", request, errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage.isEmpty() ? "参数校验失败" : errorMessage));
    }
}
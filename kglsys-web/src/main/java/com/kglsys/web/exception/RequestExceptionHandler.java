package com.kglsys.web.exception;

import com.kglsys.common.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

/**
 * 请求相关异常处理器
 * 专门处理HTTP请求相关的异常
 *
 * @author your-name
 * @since 1.0.0
 */
@RestControllerAdvice
@Order(2) // 中高优先级
public class RequestExceptionHandler extends BaseExceptionHandler {

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        String errorMessage = String.format("缺少必需的请求参数: %s", ex.getParameterName());

        logWarn("缺少请求参数", request, "参数: " + ex.getParameterName());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage));
    }

    /**
     * 处理请求参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String errorMessage = String.format("参数 '%s' 类型不正确，期望类型: %s",
                ex.getName(),
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

        logWarn("参数类型不匹配", request,
                String.format("参数: %s, 值: %s", ex.getName(), ex.getValue()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage));
    }

    /**
     * 处理请求体不可读异常（如JSON格式错误）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        logWarn("请求体解析失败", request, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "请求体格式错误，请检查JSON格式"));
    }

    /**
     * 处理请求方法不支持的异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        String supportedMethods = String.join(", ", Objects.requireNonNull(ex.getSupportedMethods()));
        String errorMessage = String.format("请求方法 '%s' 不支持，支持的方法: %s",
                ex.getMethod(), supportedMethods);

        logWarn("请求方法不支持", request,
                String.format("方法: %s, 支持的方法: [%s]", ex.getMethod(), supportedMethods));

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(405, errorMessage));
    }

    /**
     * 处理404异常
     * 注意：Spring Boot 3.x 中推荐通过 ErrorController 处理404
     * 这里保留此方法以防某些场景下仍会抛出 NoHandlerFoundException
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {

        logWarn("请求路径不存在", request, "方法: " + ex.getHttpMethod());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, "请求的资源不存在"));
    }
}
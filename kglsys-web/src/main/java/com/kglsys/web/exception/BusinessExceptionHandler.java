package com.kglsys.web.exception;

import com.kglsys.common.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：用于处理资源未找到类异常。
 * 当系统中请求的资源（如实体、数据记录等）不存在时抛出 {@link jakarta.persistence.EntityNotFoundException}，
 * 本处理器会捕获该异常并返回统一的 404 响应。
 * 使用 @Order(3) 注解指定处理器的优先级，优先级适中，适合在认证/参数异常之后，
 * 系统异常之前进行处理。
 *
 * @author your-name
 * @since 1.0.0
 */
@RestControllerAdvice
@Order(3) // 中优先级
public class BusinessExceptionHandler extends BaseExceptionHandler {

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFoundException(
            EntityNotFoundException ex, HttpServletRequest request) {

        logWarn("资源未找到", request, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, "请求的资源不存在"));
    }

    /**
     * 处理业务参数不合法异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {

        logWarn("业务参数不合法", request, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, ex.getMessage()));
    }
    /*
    使用 ProblemDetail（可选）：Spring Boot 3.x 引入了 RFC 7807 标准的 ProblemDetail，可以考虑使用
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("参数错误");
        return ResponseEntity.badRequest().body(problemDetail);
    }*/
}
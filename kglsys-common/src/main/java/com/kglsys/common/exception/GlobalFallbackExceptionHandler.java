package com.kglsys.common.exception;

import com.kglsys.common.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局兜底异常处理器
 * 处理所有未被其他异常处理器捕获的异常
 *
 * @author your-name
 * @since 1.0.0
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalFallbackExceptionHandler extends BaseExceptionHandler {

    // 针对空指针异常
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<?> handleNullPointerException(HttpServletRequest request, NullPointerException ex) {
        logError("空指针异常", request, ex);
        return ApiResponse.error(5001, "服务器发生空指针异常，请联系管理员");
    }

    // 针对非法状态异常
    @ExceptionHandler(IllegalStateException.class)
    public ApiResponse<?> handleIllegalStateException(HttpServletRequest request, IllegalStateException ex) {
        logError("非法状态异常", request, ex);
        return ApiResponse.error(5002, "服务器状态异常，请稍后重试");
    }

    // 针对类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public ApiResponse<?> handleClassCastException(HttpServletRequest request, ClassCastException ex) {
        logError("类型转换异常", request, ex);
        return ApiResponse.error(5003, "类型转换异常，请检查数据格式");
    }

    // 兜底处理
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(HttpServletRequest request, Exception ex) {
        logError("未知服务器异常", request, ex);
        return ApiResponse.error(5000, "服务器内部错误，请联系管理员");
    }
}
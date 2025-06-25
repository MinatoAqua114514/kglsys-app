package com.kglsys.web.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;

/**
 * 异常处理器基类
 * 提供通用的工具方法和日志记录功能
 *
 * @author your-name
 * @since 1.0.0
 */
public abstract class BaseExceptionHandler {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 格式化字段错误信息
     */
    protected String formatFieldError(FieldError fieldError) {
        return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
    }

    /**
     * 获取当前用户名（用于日志记录）
     */
    protected String getCurrentUserName(HttpServletRequest request) {
        try {
            // 这里可以根据你的认证机制来获取用户名
            // 例如从 SecurityContextHolder 中获取
            // 或者从 JWT token 中解析
            return request.getRemoteUser() != null ? request.getRemoteUser() : "anonymous";
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 记录错误日志的通用方法
     */
    protected void logError(String message, HttpServletRequest request, Exception ex) {
        log.error("{} - URI: {}, 方法: {}, 用户: {}",
                message,
                request.getRequestURI(),
                request.getMethod(),
                getCurrentUserName(request),
                ex);
    }

    /**
     * 记录警告日志的通用方法
     */
    protected void logWarn(String message, HttpServletRequest request, String details) {
        log.warn("{} - URI: {}, 详情: {}",
                message,
                request.getRequestURI(),
                details);
    }
}
package com.kglsys.common.exception;

import com.kglsys.common.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 数据库异常处理器
 * 专门处理数据库访问和JPA相关的异常
 *
 * @author your-name
 * @since 1.0.0
 */
@RestControllerAdvice
@Order(4) // 中低优先级
public class DatabaseExceptionHandler extends BaseExceptionHandler {

    /**
     * 处理数据完整性约束违反异常
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        logError("数据完整性约束违反", request, ex);

        // 根据具体的约束违反类型提供更友好的提示
        String message = "数据操作失败";
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Duplicate entry")) {
                message = "数据重复，该记录已存在";
            } else if (ex.getMessage().contains("foreign key constraint")) {
                message = "数据关联约束违反，无法执行操作";
            } else if (ex.getMessage().contains("cannot be null")) {
                message = "必填字段不能为空";
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, message));
    }

    /**
     * 处理数据访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleDataAccessException(
            DataAccessException ex, HttpServletRequest request) {

        logError("数据访问异常", request, ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "数据访问异常，请稍后重试"));
    }
}
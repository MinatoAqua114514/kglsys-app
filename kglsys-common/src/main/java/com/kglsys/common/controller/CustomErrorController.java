package com.kglsys.common.controller;

import com.kglsys.common.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义错误控制器
 * 用于处理 Spring Boot 3.x 中的错误页面，特别是404错误
 *
 * @author your-name
 * @since 1.0.0
 */
@RestController
public class CustomErrorController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);
    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<ApiResponse<?>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        Object errorMessage = request.getAttribute("jakarta.servlet.error.message");
        Object requestUri = request.getAttribute("jakarta.servlet.error.request_uri");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            log.warn("错误请求 - 状态码: {}, URI: {}, 错误信息: {}",
                    statusCode, requestUri, errorMessage);

            return switch (statusCode) {
                case 404 -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "请求的资源不存在"));
                case 403 -> ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(403, "访问被拒绝"));
                case 500 -> ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(500, "服务器内部错误"));
                default -> ResponseEntity
                        .status(HttpStatus.valueOf(statusCode))
                        .body(ApiResponse.error(statusCode, "请求处理失败"));
            };
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "未知错误"));
    }
}
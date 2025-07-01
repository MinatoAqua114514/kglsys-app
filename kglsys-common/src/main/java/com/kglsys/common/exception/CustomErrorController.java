package com.kglsys.common.exception;

import com.kglsys.common.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 自定义错误控制器，用于处理所有未被 @ExceptionHandler 捕获的HTTP错误，
 * 特别是 404 Not Found 错误。
 * 它会覆盖 Spring Boot 默认的 /error 端点行为。
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}") // 监听 Spring Boot 的默认错误路径
public class CustomErrorController extends AbstractErrorController {

    public CustomErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> error(HttpServletRequest request) {
        // 1. 获取 Spring Boot 默认收集的错误属性
        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());

        // 2. 从属性中提取 HTTP 状态码
        HttpStatus status = getStatus(request);

        // 3. 从属性中提取错误消息
        String message = (String) errorAttributes.getOrDefault("message", "请求的资源不存在或服务器内部错误");

        // 如果是 404 错误，提供更具体的提示
        if (status == HttpStatus.NOT_FOUND) {
            message = String.format("请求的资源不存在: %s", errorAttributes.get("path"));
        }

        // 4. 构建我们自定义的 ApiResponse
        ApiResponse<Object> body = ApiResponse.error(status.value(), message);

        // 5. 返回 ResponseEntity
        return new ResponseEntity<>(body, status);
    }
}
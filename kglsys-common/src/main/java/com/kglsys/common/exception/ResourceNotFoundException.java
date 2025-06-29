package com.kglsys.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 表示请求的资源不存在的异常。
 * 例如，根据ID查询一个不存在的用户或文章。
 * 触发此异常通常返回 HTTP 404 Not Found。
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
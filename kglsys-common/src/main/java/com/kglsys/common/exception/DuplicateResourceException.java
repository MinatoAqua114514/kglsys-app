package com.kglsys.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 表示资源重复的异常。
 * 例如，尝试创建一个已存在的用户（用户名或邮箱重复）。
 * 触发此异常通常返回 HTTP 409 Conflict。
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
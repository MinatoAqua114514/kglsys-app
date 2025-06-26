package com.kglsys.common.exception.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 自定义业务异常：当根据业务规则无法找到所需资源时抛出。
 * 例如，在Service层通过ID查询，但数据库中没有该记录。
 *
 * &#064;ResponseStatus  注解是处理异常的一种备选方式。如果没有任何@RestControllerAdvice处理此异常，
 * Spring MVC会使用此注解直接返回指定的HTTP状态码。
 * 当存在匹配的@ExceptionHandler时，@ExceptionHandler的逻辑将优先执行。
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND) // 默认情况下返回404状态码
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

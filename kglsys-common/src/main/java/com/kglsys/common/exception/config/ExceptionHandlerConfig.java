package com.kglsys.common.exception.config;

import org.springframework.context.annotation.Configuration;

/**
 * 异常处理器配置类
 * 用于管理和配置所有异常处理器
 * <p>
 * 异常处理器优先级说明：
 * - @Order(1): ValidationExceptionHandler, SecurityExceptionHandler（高优先级）
 * - @Order(2): RequestExceptionHandler（中高优先级）
 * - @Order(3): BusinessExceptionHandler（中优先级）
 * - @Order(4): DatabaseExceptionHandler（中低优先级）
 * - @Order(Integer.MAX_VALUE): GlobalFallbackExceptionHandler（最低优先级，兜底）
 * <p>
 * 异常处理器分类：
 * 1. ValidationExceptionHandler - 参数校验异常
 *    - MethodArgumentNotValidException (@RequestBody 校验)
 *    - BindException (表单校验)
 *    - ConstraintViolationException (单参数校验)
 *    - IllegalArgumentException (业务参数异常)
 * <p>
 * 2. RequestExceptionHandler - 请求相关异常
 *    - MissingServletRequestParameterException (缺少参数)
 *    - MethodArgumentTypeMismatchException (参数类型错误)
 *    - HttpMessageNotReadableException (请求体解析失败)
 *    - HttpRequestMethodNotSupportedException (请求方法不支持)
 *    - NoHandlerFoundException (404异常)
 * <p>
 * 3. SecurityExceptionHandler - 安全认证异常
 *    - AuthenticationException (认证异常基类)
 *    - BadCredentialsException (用户名密码错误)
 *    - DisabledException (账户被禁用)
 *    - LockedException (账户被锁定)
 *    - AccessDeniedException (权限不足)
 * <p>
 * 4. BusinessExceptionHandler - 业务逻辑异常
 *    - handleEntityNotFoundException (资源未找到异常)
 *    - handleIllegalArgumentException (业务参数不合法异常)
 * <p>
 * 5. DatabaseExceptionHandler - 数据库异常
 *    - EntityNotFoundException (实体未找到)
 *    - DataIntegrityViolationException (数据完整性约束)
 *    - DataAccessException (数据访问异常)
 * <p>
 * 6. GlobalFallbackExceptionHandler - 兜底异常处理器
 *    - Exception (所有未被捕获的异常)
 *
 * @author your-name
 * @since 1.0.0
 */
@Configuration
public class ExceptionHandlerConfig {

    // 这个配置类目前主要用于文档说明
    // 如果需要，可以在这里添加一些异常处理的全局配置

    /*
     * 异常处理器使用说明：
     * <p>
     * 1. 所有异常处理器都继承自 BaseExceptionHandler，提供统一的工具方法
     * 2. 使用 @Order 注解控制异常处理器的优先级
     * 3. 每个异常处理器专注于处理特定类型的异常，职责单一
     * 4. 日志记录统一，包含请求上下文信息
     * 5. 错误响应格式统一，使用 ApiResponse 包装
     * <p>
     * 扩展方式：
     * - 如果需要添加新的异常类型，创建对应的异常处理器类
     * - 继承 BaseExceptionHandler 获得通用功能
     * - 使用 @RestControllerAdvice 和 @Order 注解
     * - 在对应的处理器中添加 @ExceptionHandler 方法
     */
}
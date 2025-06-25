package com.kglsys.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.dao.DataIntegrityViolationException;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('user:read')")
    public String hello() {
        return "Hello, World!";
    }

    // 1. 参数校验异常
    @GetMapping("/validation")
    public void testValidationException() {
        throw new IllegalArgumentException("参数不合法测试");
    }

    // 2. 请求相关异常
    @GetMapping("/request")
    public void testRequestException() throws MissingServletRequestParameterException {
        throw new org.springframework.web.bind.MissingServletRequestParameterException("param", "String");
    }

    // 3. 安全认证异常
    @GetMapping("/security")
    public void testSecurityException() {
        throw new BadCredentialsException("用户名或密码错误测试");
    }

    // 4. 权限不足异常
    @GetMapping("/access-denied")
    public void testAccessDeniedException() {
        throw new AccessDeniedException("没有权限访问测试");
    }

    // 5. 业务异常
    @GetMapping("/business")
    public void testBusinessException() {
        throw new RuntimeException("业务异常测试");
    }

    // 6. 数据库异常
    @GetMapping("/database")
    public void testDatabaseException() {
        throw new DataIntegrityViolationException("数据库约束异常测试");
    }

    // 7. 兜底异常
    @GetMapping("/fallback")
    public void testFallbackException() {
        throw new NullPointerException("兜底异常测试");
    }

    // 8. 空指针异常
    @GetMapping("/null-pointer")
    public void testNullPointerException() {
        throw new NullPointerException("空指针异常测试");
    }

    // 9. 非法状态异常
    @GetMapping("/illegal-state")
    public void testIllegalStateException() {
        throw new IllegalStateException("非法状态异常测试");
    }

    // 10. 类型转换异常
    @GetMapping("/class-cast")
    public void testClassCastException() {
        throw new ClassCastException("类型转换异常测试");
    }
}

package com.kglsys.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

// 当data为null时，不序列化该字段
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {

    private final int code;       // 状态码 (例如: 200, 401, 500)
    private final String message;   // 响应消息
    private final T data;         // 响应数据

    // 私有构造函数
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // --- 静态工厂方法 ---

    // 成功，并返回数据
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    // 成功，返回自定义消息和数据
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    // 成功，不返回数据
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "Success", null);
    }

    // 失败，返回指定状态码和消息
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
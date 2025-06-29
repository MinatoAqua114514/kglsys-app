package com.kglsys.common.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * 全局统一API响应体。
 * 所有Controller的返回值都应封装在此对象中，为前端提供一致的数据结构。
 * @param <T> 响应体中`data`字段的类型。
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // 关键优化：如果data字段为null，则不序列化该字段
public class ApiResponse<T> {

    /**
     * 状态码 (通常与HTTP状态码一致, 如 200, 400, 404, 500)。
     */
    private final int code;
    /**
     * 响应消息 (如 "Success", "用户不存在")。
     */
    private final String message;
    /**
     * 实际的响应数据。
     */
    private final T data;

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建一个表示成功的响应，包含数据。
     * @param data 响应数据。
     * @return 包含数据的成功响应。
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    /**
     * 创建一个表示成功的响应，不包含数据 (通常用于POST, PUT, DELETE等操作)。
     * @return 不包含数据的成功响应。
     */
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(200, "Success", null);
    }

    /**
     * 创建一个表示错误的响应。
     * @param code 错误码。
     * @param message 错误信息。
     * @return 错误响应。
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * 创建一个表示错误的响应，并可携带额外的错误数据 (如参数校验失败的字段列表)。
     * @param code 错误码。
     * @param message 错误信息。
     * @param data 额外的错误数据。
     * @return 包含错误数据的错误响应。
     */
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
}
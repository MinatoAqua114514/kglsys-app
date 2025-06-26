package com.kglsys.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 接收前端提交答案的请求体 (DTO)
 */
@Data // Lombok 注解，自动生成 getter, setter, toString, equals, hashCode
public class AnswerSubmissionRequest {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 题目ID
     */
    @NotNull(message = "题目ID不能为空")
    private Integer questionId;

    /**
     * 用户选择的选项ID
     */
    @NotNull(message = "选项ID不能为空")
    private Integer selectedOptionId;
}
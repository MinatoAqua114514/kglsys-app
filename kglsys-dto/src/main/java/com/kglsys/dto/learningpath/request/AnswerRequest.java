package com.kglsys.dto.learningpath.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 代表单个答案的数据载体。
 * 用于在 SubmitAssessmentRequest 中传递数据。
 */
@Data
public class AnswerRequest {

    @NotNull(message = "问题ID不能为空")
    private Integer questionId;

    @NotNull(message = "所选选项ID不能为空")
    private Integer selectedOptionId;
}
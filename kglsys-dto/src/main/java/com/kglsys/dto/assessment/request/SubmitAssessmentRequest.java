package com.kglsys.dto.assessment.request;

import com.kglsys.dto.learningpath.request.AnswerRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

/**
 * 学生提交测评问卷的请求DTO。
 */
@Data
public class SubmitAssessmentRequest {

    /**
     * 学生提交的答案列表。
     * @Valid 注解会级联校验列表中的每个 AnswerPayload 对象。
     */
    @NotEmpty(message = "答案列表不能为空")
    @Valid
    private List<AnswerRequest> answers;
}
package com.kglsys.dto.assessment.request;

import com.kglsys.dto.learningpath.request.QuestionOptionRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

/**
 * 用于创建或更新测评问题的请求DTO。
 */
@Data
public class AssessmentQuestionRequest {

    @NotBlank(message = "问题内容不能为空")
    private String questionText;

    @NotNull(message = "问题顺序不能为空")
    private Integer sequence;

    private boolean isActive = true;

    /**
     * 该问题关联的选项列表。
     * @Valid 注解会级联校验列表中的每个 QuestionOptionRequest 对象。
     */
    @Valid
    @NotEmpty(message = "问题至少需要一个选项")
    private List<QuestionOptionRequest> options;
}
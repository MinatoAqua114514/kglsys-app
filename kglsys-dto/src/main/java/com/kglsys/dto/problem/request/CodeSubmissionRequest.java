package com.kglsys.dto.problem.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CodeSubmissionRequest {
    @NotNull(message = "题目ID不能为空")
    private Long problemId;

    @NotBlank(message = "编程语言不能为空")
    private String language;

    @NotBlank(message = "源代码不能为空")
    private String sourceCode;
}
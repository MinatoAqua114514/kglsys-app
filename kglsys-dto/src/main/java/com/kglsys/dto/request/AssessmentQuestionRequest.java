package com.kglsys.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssessmentQuestionRequest {
    @NotBlank
    private String questionText;
    @NotNull
    private Integer sequence;
    private boolean isActive = true;
    @Valid
    private List<QuestionOptionRequest> options = new ArrayList<>();
}
package com.kglsys.dto.request;

import com.kglsys.dto.AnswerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SubmitAssessmentRequest {
    @NotEmpty
    @Valid
    private List<AnswerDto> answers;
}
package com.kglsys.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerDto {
    @NotNull
    private Integer questionId;
    @NotNull
    private Integer selectedOptionId;
}
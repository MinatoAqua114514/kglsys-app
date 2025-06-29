package com.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionOptionRequest {
    private Integer id; // for updates
    @NotBlank
    private String optionText;
    @NotNull
    private Integer contributesToStyleId;
}
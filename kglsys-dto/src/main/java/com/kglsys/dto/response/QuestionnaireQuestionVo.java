package com.kglsys.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireQuestionVo {
    private Integer id;
    private String questionText;
    private List<QuestionOptionVo> options;
}
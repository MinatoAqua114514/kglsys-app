package com.kglsys.dto.learningpath.response;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireQuestionVo {
    private Integer id;
    private String questionText;
    private List<QuestionOptionVo> options;
}
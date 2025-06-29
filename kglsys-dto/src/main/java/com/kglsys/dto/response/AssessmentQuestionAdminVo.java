package com.kglsys.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class AssessmentQuestionAdminVo {

    private Integer id;

    private String questionText;

    private Integer sequence;

    private boolean isActive;

    private List<QuestionOptionAdminVo> options;
}
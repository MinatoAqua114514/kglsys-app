package com.kglsys.dto.learningpath.response;

import lombok.Data;

@Data
public class QuestionOptionAdminVo {

    private Integer id;

    private String optionText;

    private Integer contributesToStyleId;

    private String contributesToStyleName;
}
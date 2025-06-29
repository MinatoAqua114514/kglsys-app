package com.kglsys.dto.response;

import lombok.Data;

@Data
public class QuestionOptionAdminVo {

    private Integer id;

    private String optionText;

    private Integer contributesToStyleId;

    private String contributesToStyleName;
}
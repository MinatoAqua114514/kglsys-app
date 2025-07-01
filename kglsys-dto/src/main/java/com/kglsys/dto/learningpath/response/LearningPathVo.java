package com.kglsys.dto.learningpath.response;

import lombok.Data;

@Data
public class LearningPathVo {
    private Long id;
    private String name;
    private String description;
    private String difficultyLevel;
}
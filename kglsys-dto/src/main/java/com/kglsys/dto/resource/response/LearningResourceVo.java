package com.kglsys.dto.resource.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningResourceVo {
    private Long id;
    private String title;
    private String resourceType;
    private String difficultyLevel;
    private String contentUrl;
}
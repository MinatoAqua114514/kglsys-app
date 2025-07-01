package com.kglsys.dto.resource.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningResourceDetailVo {
    private Long id;
    private String title;
    private String description;
    private String resourceType;
    private String contentUrl;
    private String content; // The full content for articles/tutorials
    private String difficultyLevel;
    private Integer estimatedDuration;
}
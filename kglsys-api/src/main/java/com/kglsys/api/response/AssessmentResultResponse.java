package com.kglsys.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentResultResponse {
    private LearningStyleDTO learningStyle;
    private List<LearningPathDTO> recommendedPaths;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LearningStyleDTO {
        private Integer id;
        private String displayName;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LearningPathDTO {
        private Long id;
        private String name;
        private String description;
    }
}
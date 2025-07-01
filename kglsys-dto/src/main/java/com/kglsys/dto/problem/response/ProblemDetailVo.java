package com.kglsys.dto.problem.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemDetailVo {
    private Long id;
    private String title;
    private String description; // Full description with I/O formats, examples etc.
    private String difficulty;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String tags;
}
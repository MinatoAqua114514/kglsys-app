package com.kglsys.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemSummaryVo {
    private Long id;
    private String title;
    private String difficulty;
}
package com.kglsys.dto.nodedetail;

import com.kglsys.domain.enums.Difficulty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemSummaryDTO {
    private Long id;
    private String title;
    private Difficulty difficulty;
}
package com.kglsys.dto.problem.response;

import com.kglsys.domain.problem.enums.SubmissionStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SubmissionResultVo {
    private Long id;
    private SubmissionStatus status;
    private Integer executionTimeMs;
    private Integer memoryUsedKb;
    private String judgeOutput;
    private LocalDateTime submittedAt;
}
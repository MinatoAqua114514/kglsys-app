package com.kglsys.dto.learningpath.request;

import com.kglsys.domain.learningpath.enums.LearningStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNodeStatusRequest {
    @NotNull(message = "Learning status cannot be null.")
    private LearningStatus status;
}
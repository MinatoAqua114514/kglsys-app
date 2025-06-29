package com.kglsys.dto.request;

import com.kglsys.domain.enums.LearningStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNodeStatusRequest {
    @NotNull(message = "Learning status cannot be null.")
    private LearningStatus status;
}
package com.kglsys.dto.learningpath.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SelectPathRequest {
    @NotNull
    private Long pathId;
}
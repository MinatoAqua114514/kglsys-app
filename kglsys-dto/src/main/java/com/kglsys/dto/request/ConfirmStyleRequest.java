package com.kglsys.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmStyleRequest {
    @NotNull
    private Integer learningStyleId;
}
package com.kglsys.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusUpdateRequest {
    @NotNull(message = "启用状态不能为空")
    private Boolean enabled;
}
package com.kglsys.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRoleUpdateRequest {
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
}
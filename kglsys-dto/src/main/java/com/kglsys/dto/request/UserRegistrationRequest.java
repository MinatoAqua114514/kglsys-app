package com.kglsys.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 用户注册请求的数据传输对象。
 */
@Data
public class UserRegistrationRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3到50之间")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8到20之间")
    private String password;

    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "STUDENT|TEACHER", message = "角色必须是'STUDENT'或'TEACHER'")
    private String roleName;
}
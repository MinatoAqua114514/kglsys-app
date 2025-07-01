package com.kglsys.dto.user.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户个人资料的请求DTO。
 * 只包含可被用户修改的字段。
 */
@Data
public class UpdateUserProfileRequest {

    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String fullName;

    @Size(max = 512, message = "头像URL过长")
    private String avatarUrl;

    @Size(max = 20, message = "电话号码长度不能超过20个字符")
    private String phoneNumber;

    @Size(max = 50, message = "学号长度不能超过50个字符")
    private String studentId;

    @Size(max = 100, message = "院系/部门长度不能超过100个字符")
    private String department;

    @Size(max = 100, message = "职称长度不能超过100个字符")
    private String title;
}
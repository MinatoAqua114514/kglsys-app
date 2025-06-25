package com.kglsys.api.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserProfileRequest {
    /**
     * 用户ID - 使用 @NotNull 而不是 @NotBlank
     */
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    /**
     * 真实姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String fullName;

    /**
     * 头像URL
     */
    @Size(max = 512, message = "头像URL长度不能超过512个字符")
    // @Pattern(regexp = "^(https?://.*)?$", message = "头像URL格式不正确")
    private String avatarUrl;

    /**
     * 联系电话
     */
    @Size(max = 20, message = "电话号码长度不能超过20个字符")
    // @Pattern(regexp = "^[\\d\\s\\-\\+\\(\\)]*$", message = "电话号码格式不正确")
    private String phoneNumber;

    /**
     * 学号（学生特有）
     */
    @Size(max = 50, message = "学号长度不能超过50个字符")
    private String studentId;

    /**
     * 院系或部门
     */
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String department;

    /**
     * 职称（教师特有）
     */
    @Size(max = 100, message = "职称长度不能超过100个字符")
    private String title;
}
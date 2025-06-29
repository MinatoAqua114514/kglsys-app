package com.kglsys.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileVo {

    private Long userId;

    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String fullName;

    @Size(max = 512, message = "头像URL过长")
    private String avatarUrl;

    @Size(max = 20, message = "电话号码长度不能超过20个字符")
    private String phoneNumber;

    // 学生特有
    @Size(max = 50, message = "学号长度不能超过50个字符")
    private String studentId;

    @Size(max = 100, message = "院系/部门长度不能超过100个字符")
    private String department;

    // 教师特有
    @Size(max = 100, message = "职称长度不能超过100个字符")
    private String title;
}
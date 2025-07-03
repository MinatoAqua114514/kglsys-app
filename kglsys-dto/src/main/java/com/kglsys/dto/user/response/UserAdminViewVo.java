package com.kglsys.dto.user.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 专用于管理员用户管理界面的用户视图对象。
 */
@Data
@Builder
public class UserAdminViewVo {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private boolean accountNonLocked;
    private LocalDateTime createdAt;
    private List<String> roles;
}
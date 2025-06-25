package com.kglsys.api.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户个人资料响应 DTO
 * 避免循环引用问题，只包含前端需要的字段
 * <p>
 * 问题分析
 * DTO 位置: UserProfileResponse 和其内部类 UserBasicInfo 都应该位于 rbac-api 模块。
 * 实体位置: UserProfileEntity, UserEntity, RoleEntity 都位于 rbac-domain 模块。
 * 冲突点: fromEntity 静态方法再次导致了 rbac-api 对 rbac-domain 的非法依赖。
 * 复杂性: 这次转换涉及到了嵌套对象 (UserBasicInfo) 和集合处理 (Set<RoleEntity> -> Set<String>)，手动编写转换代码会比较繁琐且容易出错。
 * 解决方案：使用 MapStruct 处理嵌套映射
 * 我们将再次使用 Mapper 模式，将转换逻辑放在 rbac-application 中。MapStruct 对于这种嵌套对象的映射处理得非常优雅。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private String phoneNumber;
    private String studentId;
    private String department;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 用户基本信息（避免循环引用）
    private UserBasicInfo user;

    /**
     * 用户基本信息内嵌类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserBasicInfo {
        private Long id;
        private String username;
        private String email;
        private boolean enabled;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Set<String> roleNames; // 只返回角色名称，避免复杂对象
    }
}
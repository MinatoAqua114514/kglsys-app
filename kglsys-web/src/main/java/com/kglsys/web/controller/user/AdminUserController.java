package com.kglsys.web.controller.user;

import com.kglsys.application.service.user.AdminUserService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.user.request.UserRoleUpdateRequest;
import com.kglsys.dto.user.request.UserStatusUpdateRequest;
import com.kglsys.dto.user.response.UserAdminViewVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // 整个Controller都需要ADMIN角色
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 获取用户列表，支持分页和搜索。
     * @param username (可选) 按用户名模糊搜索
     * @param email (可选) 按邮箱模糊搜索
     * @param pageable (自动注入) 分页参数，例如 ?page=0&size=10&sort=createdAt,desc
     * @return 分页的用户数据
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserAdminViewVo>>> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @PageableDefault(sort = "id") Pageable pageable) {
        Page<UserAdminViewVo> userPage = adminUserService.listUsers(username, email, pageable);
        return ResponseEntity.ok(ApiResponse.success(userPage));
    }

    /**
     * 更新用户的角色。
     * @param userId 目标用户ID
     * @param request 请求体，包含新的角色名
     * @return 更新后的用户信息
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<UserAdminViewVo>> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UserRoleUpdateRequest request) {
        UserAdminViewVo updatedUser = adminUserService.updateUserRole(userId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    /**
     * 更新用户的启用/禁用状态。
     * @param userId 目标用户ID
     * @param request 请求体，包含新的状态
     * @return 更新后的用户信息
     */
    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<UserAdminViewVo>> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UserStatusUpdateRequest request) {
        UserAdminViewVo updatedUser = adminUserService.updateUserStatus(userId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }
}
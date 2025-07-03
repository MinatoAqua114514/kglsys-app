package com.kglsys.application.service.user;

import com.kglsys.dto.user.request.UserRoleUpdateRequest;
import com.kglsys.dto.user.request.UserStatusUpdateRequest;
import com.kglsys.dto.user.response.UserAdminViewVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    /**
     * 分页并按条件搜索用户列表。
     * @param username 可选的用户名搜索条件
     * @param email 可选的邮箱搜索条件
     * @param pageable 分页信息
     * @return 分页的用户视图对象
     */
    Page<UserAdminViewVo> listUsers(String username, String email, Pageable pageable);

    /**
     * 更新指定用户的角色。
     * @param userId 用户ID
     * @param request 包含新角色名称的请求
     * @return 更新后的用户视图对象
     */
    UserAdminViewVo updateUserRole(Long userId, UserRoleUpdateRequest request);

    /**
     * 更新指定用户的启用状态。
     * @param userId 用户ID
     * @param request 包含新状态的请求
     * @return 更新后的用户视图对象
     */
    UserAdminViewVo updateUserStatus(Long userId, UserStatusUpdateRequest request);
}
package com.kglsys.web.controller;

import com.kglsys.application.service.UserProfileService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.request.UpdateUserProfileRequest;
import com.kglsys.dto.response.UserProfileVo;
import com.kglsys.application.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 处理用户个人资料相关操作的API。
 */
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * 获取当前登录用户的个人资料。
     * @return 包含用户资料的响应。
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserProfileVo>> getMyProfile() {
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("无法获取当前用户信息"));
        UserProfileVo profile = userProfileService.getProfileByUserId(currentUserId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    /**
     * 更新当前登录用户的个人资料。
     * @param profileDto 包含待更新字段的请求体。
     * @return 包含更新后用户资料的响应。
     */
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserProfileVo>> updateMyProfile(@Valid @RequestBody UpdateUserProfileRequest profileDto) {
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("无法获取当前用户信息"));
        UserProfileVo updatedProfile = userProfileService.createOrUpdateProfile(currentUserId, profileDto);
        return ResponseEntity.ok(ApiResponse.success(updatedProfile));
    }

    /**
     * [ADMIN] 获取指定用户的个人资料。
     * @param userId 目标用户的ID。
     * @return 包含用户资料的响应。
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileVo>> getUserProfile(@PathVariable Long userId) {
        UserProfileVo profile = userProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    /**
     * [ADMIN] 更新指定用户的个人资料。
     * @param userId 目标用户的ID。
     * @param profileDto 包含待更新字段的请求体。
     * @return 包含更新后用户资料的响应。
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileVo>> updateUserProfile(@PathVariable Long userId, @Valid @RequestBody UpdateUserProfileRequest profileDto) {
        UserProfileVo updatedProfile = userProfileService.createOrUpdateProfile(userId, profileDto);
        return ResponseEntity.ok(ApiResponse.success(updatedProfile));
    }

    // [ADMIN] 删除任意用户的资料
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUserProfile(@PathVariable Long userId) {
        userProfileService.deleteProfile(userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
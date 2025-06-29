package com.kglsys.web.controller;

import com.kglsys.application.service.UserProfileService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.UserProfileDto;
import com.kglsys.application.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // 获取自己的资料
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile() {
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("无法获取当前用户信息"));
        UserProfileDto profile = userProfileService.getProfileByUserId(currentUserId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    // 更新自己的资料
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateMyProfile(@Valid @RequestBody UserProfileDto profileDto) {
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("无法获取当前用户信息"));
        UserProfileDto updatedProfile = userProfileService.createOrUpdateProfile(currentUserId, profileDto);
        return ResponseEntity.ok(ApiResponse.success(updatedProfile));
    }

    // [ADMIN] 获取任意用户的资料
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(@PathVariable Long userId) {
        UserProfileDto profile = userProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    // [ADMIN] 更新任意用户的资料
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateUserProfile(@PathVariable Long userId, @Valid @RequestBody UserProfileDto profileDto) {
        UserProfileDto updatedProfile = userProfileService.createOrUpdateProfile(userId, profileDto);
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
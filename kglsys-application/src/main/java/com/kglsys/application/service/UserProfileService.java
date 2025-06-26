package com.kglsys.application.service;

import com.kglsys.api.request.UserProfileRequest;
import com.kglsys.api.response.UserProfileResponse;
import com.kglsys.domain.entity.base.UserEntity;
import com.kglsys.domain.entity.base.UserProfileEntity;
import com.kglsys.domain.repository.base.UserProfileRepository;
import com.kglsys.domain.repository.base.UserRepository;
import com.kglsys.application.mapper.UserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserProfileMapper userProfileMapper;

    /**
     * 创建用户个人资料
     * 职责：仅在用户资料不存在时创建新的个人资料
     *
     * @param request 用户资料请求
     * @return 创建的用户资料响应
     * @throws IllegalStateException 当用户资料已存在时
     * @throws IllegalArgumentException 当用户不存在或参数无效时
     */
    @Transactional
    public UserProfileResponse createUserProfile(UserProfileRequest request) {

        // 1. 验证用户是否存在
        UserEntity user = validateUserExists(request.getUserId());

        // 2. 检查是否已有个人资料（严格检查，不允许重复创建）
        if (userProfileRepository.existsById(request.getUserId())) {
            throw new IllegalStateException("用户个人资料已存在，请使用更新接口修改资料");
        }

        // 3. 角色验证
        validateRoleBasedFields(user, request);

        // 4. 创建新的个人资料
        UserProfileEntity profile = new UserProfileEntity();
        profile.setUser(user);
        updateProfileFields(profile, request, user);

        UserProfileEntity savedProfile = userProfileRepository.save(profile);
        return userProfileMapper.toUserProfileResponse(savedProfile);
    }

    /**
     * 更新用户个人资料
     * 职责：仅更新已存在的用户资料
     *
     * @param request 用户资料请求
     * @return 更新后的用户资料响应
     * @throws IllegalStateException 当用户资料不存在时
     * @throws IllegalArgumentException 当用户不存在或参数无效时
     */
    @Transactional
    public UserProfileResponse updateUserProfile(UserProfileRequest request) {

        // 1. 验证用户是否存在
        UserEntity user = validateUserExists(request.getUserId());

        // 2. 获取现有的个人资料
        UserProfileEntity profile = userProfileRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("用户个人资料不存在，请先创建"));

        // 3. 角色验证
        validateRoleBasedFields(user, request);

        // 4. 更新字段
        updateProfileFields(profile, request, user);

        UserProfileEntity savedProfile = userProfileRepository.save(profile);
        return userProfileMapper.toUserProfileResponse(savedProfile);
    }

    /**
     * 创建或更新用户个人资料（兼容性方法）
     * 职责：智能判断是创建还是更新操作
     * 使用 @Transactional 注解，确保整个方法在一个事务中执行。
     * 这对于先读后写的操作至关重要，能保证数据的一致性。
     *
     * @param request 用户资料请求
     * @return 保存后的用户资料响应
     * @throws IllegalArgumentException 当用户不存在或参数无效时
     */
    @Transactional
    public UserProfileResponse saveUserProfile(UserProfileRequest request) {

        // 1. 验证用户是否存在
        UserEntity user = validateUserExists(request.getUserId());

        // 2. 角色验证
        validateRoleBasedFields(user, request);

        // 3. 查找或创建 UserProfileEntity
        // 【核心修改】使用 orElseGet 来延迟新对象的创建
        UserProfileEntity profile = userProfileRepository.findById(request.getUserId())
                .orElseGet(() -> {
                    UserProfileEntity newProfile = new UserProfileEntity();
                    newProfile.setUser(user);
                    return newProfile;
                });

        // 4. 更新字段
        updateProfileFields(profile, request, user);

        UserProfileEntity savedProfile = userProfileRepository.save(profile);

        boolean isNewProfile = profile.getCreatedAt() == null ||
                profile.getCreatedAt().equals(savedProfile.getCreatedAt());
        System.out.println("--- LOG: User profile " + (isNewProfile ? "created" : "updated") +
                " successfully for userId: " + request.getUserId() + " ---");

        return userProfileMapper.toUserProfileResponse(savedProfile);
    }


    /**
     * 删除用户个人资料
     */
    @Transactional
    public void deleteUserProfile(Long userId) {

        // 1. 验证用户是否存在
        validateUserExists(userId);

        // 2. 检查个人资料是否存在
        if (!userProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("用户个人资料不存在，无法删除");
        }

        // 3. 执行删除
        userProfileRepository.deleteById(userId);
    }


    // ================== 私有辅助方法 ==================

    /**
     * 获取用户个人资料
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        UserProfileEntity entity = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户个人资料不存在"));
        return userProfileMapper.toUserProfileResponse(entity);
    }

    /**
     * 检查用户个人资料是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsUserProfile(Long userId) {
        return userProfileRepository.existsById(userId);
    }

    /**
     * 验证用户是否存在
     */
    private UserEntity validateUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + userId));
    }

    /**
     * 基于角色的字段验证
     */
    private void validateRoleBasedFields(UserEntity user, UserProfileRequest request) {
        String role = user.getRoles().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("用户没有分配角色"))
                .getName();

        String title = request.getTitle();
        String studentId = request.getStudentId();

        if ("student".equals(role) && title != null && !title.trim().isEmpty()) {
            throw new IllegalArgumentException("学生角色不允许填写职称");
        }
        if ("teacher".equals(role) && studentId != null && !studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("教师角色不允许填写学号");
        }
    }

    /**
     * 更新个人资料字段
     */
    private void updateProfileFields(UserProfileEntity profile, UserProfileRequest request, UserEntity user) {
        String role = user.getRoles().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("用户没有分配角色"))
                .getName();

        // 更新通用字段
        profile.setFullName(request.getFullName());
        profile.setAvatarUrl(request.getAvatarUrl());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setDepartment(request.getDepartment());

        // 根据角色设置特定字段
        if ("student".equals(role)) {
            profile.setStudentId(request.getStudentId());
            profile.setTitle(null); // 清空职称
        } else if ("teacher".equals(role)) {
            profile.setTitle(request.getTitle());
            profile.setStudentId(null); // 清空学号
        }
    }
}
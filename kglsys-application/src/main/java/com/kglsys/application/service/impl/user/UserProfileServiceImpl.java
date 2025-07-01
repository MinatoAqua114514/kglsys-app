package com.kglsys.application.service.impl.user;

import com.kglsys.application.mapper.user.UserProfileMapper;
import com.kglsys.application.service.user.UserProfileService;
import com.kglsys.common.exception.BusinessRuleException;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.user.User;
import com.kglsys.domain.user.UserProfile;
import com.kglsys.dto.user.request.UpdateUserProfileRequest;
import com.kglsys.dto.user.response.UserProfileVo;
import com.kglsys.infra.repository.user.UserProfileRepository;
import com.kglsys.infra.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户个人资料服务的实现类。
 */
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public UserProfileVo getProfileByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户资料未找到，用户ID: " + userId));
        return userProfileMapper.toVo(profile);
    }

    @Override
    @Transactional
    public UserProfileVo createOrUpdateProfile(Long userId, UpdateUserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户未找到，用户ID: " + userId));

        // 业务规则校验
        validateProfileByRole(user, request);

        UserProfile profile = userProfileRepository.findById(userId)
                .orElseGet(() -> {
                    // 如果Profile不存在，创建一个新的
                    UserProfile newProfile = new UserProfile();
                    // 【关键】维护双向关系
                    user.setUserProfile(newProfile);
                    return newProfile;
                });

        // 使用 Mapper 更新
        userProfileMapper.updateFromRequest(request, profile);

        // 保存 User 会级联保存/更新 Profile
        userRepository.save(user);

        // 使用 Mapper 转换返回结果
        return userProfileMapper.toVo(profile);
    }

    @Override
    @Transactional
    public void deleteProfile(Long userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户资料不存在，无法删除，用户ID: " + userId);
        }
        // 由于 User 和 UserProfile 的级联关系，直接删除 profile 即可
        userProfileRepository.deleteById(userId);
    }

    private void validateProfileByRole(User user, UpdateUserProfileRequest request) {
        boolean isStudent = user.getRoles().stream().anyMatch(role -> "STUDENT".equals(role.getName()));
        boolean isTeacher = user.getRoles().stream().anyMatch(role -> "TEACHER".equals(role.getName()));

        if (isStudent && request.getTitle() != null) {
            throw new BusinessRuleException("学生角色不允许设置职称(title)");
        }
        if (isTeacher && request.getStudentId() != null) {
            throw new BusinessRuleException("教师角色不允许设置学号(studentId)");
        }
    }
}
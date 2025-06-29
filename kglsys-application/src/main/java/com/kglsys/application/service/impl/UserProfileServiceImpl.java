package com.kglsys.application.service.impl;

import com.kglsys.application.service.UserProfileService;
import com.kglsys.common.exception.BusinessRuleException;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.entity.Role;
import com.kglsys.domain.entity.User;
import com.kglsys.domain.entity.UserProfile;
import com.kglsys.dto.UserProfileDto;
import com.kglsys.infra.repository.UserProfileRepository;
import com.kglsys.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    // MapStruct 在这种简单场景下可能过度设计，我们手动映射。
    // 如果对象复杂，推荐使用 MapStruct。

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getProfileByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户资料未找到，用户ID: " + userId));
        return mapEntityToDto(profile);
    }

    @Override
    @Transactional
    public UserProfileDto createOrUpdateProfile(Long userId, UserProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户未找到，用户ID: " + userId));

        // 业务规则校验
        validateProfileByRole(user, profileDto);

        UserProfile profile = userProfileRepository.findById(userId)
                .orElse(new UserProfile());

        mapDtoToEntity(profileDto, profile);
        profile.setUser(user); // 确保关联关系正确

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapEntityToDto(savedProfile);
    }

    @Override
    @Transactional
    public void deleteProfile(Long userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户资料不存在，无法删除，用户ID: " + userId);
        }
        userProfileRepository.deleteById(userId);
    }

    private void validateProfileByRole(User user, UserProfileDto dto) {
        boolean isStudent = user.getRoles().stream().anyMatch(role -> "STUDENT".equals(role.getName()));
        boolean isTeacher = user.getRoles().stream().anyMatch(role -> "TEACHER".equals(role.getName()));

        if (isStudent && dto.getTitle() != null) {
            throw new BusinessRuleException("学生角色不允许设置职称(title)");
        }
        if (isTeacher && dto.getStudentId() != null) {
            throw new BusinessRuleException("教师角色不允许设置学号(studentId)");
        }
    }

    // Manual Mappers
    private UserProfileDto mapEntityToDto(UserProfile entity) {
        UserProfileDto dto = new UserProfileDto();
        dto.setUserId(entity.getUserId());
        dto.setFullName(entity.getFullName());
        dto.setAvatarUrl(entity.getAvatarUrl());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setStudentId(entity.getStudentId());
        dto.setDepartment(entity.getDepartment());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    private void mapDtoToEntity(UserProfileDto dto, UserProfile entity) {
        entity.setFullName(dto.getFullName());
        entity.setAvatarUrl(dto.getAvatarUrl());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setStudentId(dto.getStudentId());
        entity.setDepartment(dto.getDepartment());
        entity.setTitle(dto.getTitle());
    }
}
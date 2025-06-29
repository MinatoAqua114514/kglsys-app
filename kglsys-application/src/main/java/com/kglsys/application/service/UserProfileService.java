package com.kglsys.application.service;

import com.kglsys.dto.UserProfileDto;

public interface UserProfileService {
    UserProfileDto getProfileByUserId(Long userId);
    UserProfileDto createOrUpdateProfile(Long userId, UserProfileDto profileDto);
    void deleteProfile(Long userId);
}
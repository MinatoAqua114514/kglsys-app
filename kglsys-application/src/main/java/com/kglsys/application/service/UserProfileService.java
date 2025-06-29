package com.kglsys.application.service;

import com.kglsys.dto.response.UserProfileVo;

public interface UserProfileService {
    UserProfileVo getProfileByUserId(Long userId);
    UserProfileVo createOrUpdateProfile(Long userId, UserProfileVo profileDto);
    void deleteProfile(Long userId);
}
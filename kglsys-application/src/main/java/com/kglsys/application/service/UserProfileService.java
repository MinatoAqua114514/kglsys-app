package com.kglsys.application.service;

import com.kglsys.dto.request.UpdateUserProfileRequest;
import com.kglsys.dto.response.UserProfileVo;

public interface UserProfileService {
    UserProfileVo getProfileByUserId(Long userId);
    UserProfileVo createOrUpdateProfile(Long userId, UpdateUserProfileRequest request);
    void deleteProfile(Long userId);
}
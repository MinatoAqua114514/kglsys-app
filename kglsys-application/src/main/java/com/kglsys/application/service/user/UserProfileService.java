package com.kglsys.application.service.user;

import com.kglsys.dto.user.request.UpdateUserProfileRequest;
import com.kglsys.dto.user.response.UserProfileVo;

public interface UserProfileService {
    UserProfileVo getProfileByUserId(Long userId);
    UserProfileVo createOrUpdateProfile(Long userId, UpdateUserProfileRequest request);
    void deleteProfile(Long userId);
}
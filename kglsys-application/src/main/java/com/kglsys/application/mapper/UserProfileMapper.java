package com.kglsys.application.mapper;

import com.kglsys.domain.entity.UserProfile;
import com.kglsys.dto.request.UpdateUserProfileRequest;
import com.kglsys.dto.response.UserProfileVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        // 关键：当请求DTO中的字段为null时，不要更新到实体中对应的字段
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {

    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfileVo toVo(UserProfile entity);

    // 更新时，忽略userId，因为它不能被修改
    @Mapping(target = "userId", ignore = true)
    void updateFromRequest(UpdateUserProfileRequest request, @MappingTarget UserProfile entity);
}
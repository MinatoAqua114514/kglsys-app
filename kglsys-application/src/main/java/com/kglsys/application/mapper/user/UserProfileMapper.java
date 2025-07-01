package com.kglsys.application.mapper.user;

import com.kglsys.domain.user.UserProfile;
import com.kglsys.dto.user.request.UpdateUserProfileRequest;
import com.kglsys.dto.user.response.UserProfileVo;
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

    /**
     * 【核心修正】在更新方法上，忽略所有不应由请求DTO修改的字段。
     * @param request 源DTO
     * @param entity 目标实体
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "user", ignore = true) // 【修正】忽略 user 关联对象
    @Mapping(target = "createdAt", ignore = true) // 【修正】忽略创建时间
    @Mapping(target = "updatedAt", ignore = true) // 【修正】忽略更新时间
    void updateFromRequest(UpdateUserProfileRequest request, @MappingTarget UserProfile entity);
}
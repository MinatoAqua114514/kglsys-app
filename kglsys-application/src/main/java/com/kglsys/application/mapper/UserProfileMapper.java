package com.kglsys.application.mapper;

import com.kglsys.api.response.UserProfileResponse;
import com.kglsys.domain.entity.base.RoleEntity;
import com.kglsys.domain.entity.base.UserEntity;
import com.kglsys.domain.entity.base.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代码解读:
 * <p>
 * 我们定义了主方法 toUserProfileResponse(UserProfileEntity entity)。
 * 当 MapStruct 处理 user 字段时，它发现源类型是 UserEntity，目标类型是 UserProfileResponse.UserBasicInfo。于是，它会在这个 Mapper 接口里寻找一个参数和返回类型匹配的方法，并找到了 userEntityToUserBasicInfo，然后自动调用它。
 * 在 userEntityToUserBasicInfo 方法中，我们再次使用 @Mapping 和 @Named 来处理 roles 到 roleNames 的集合转换。
 * 这样，整个复杂的嵌套转换过程就被拆解成了几个清晰、声明式的方法。
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    /**
     * 主转换方法：将 UserProfileEntity 转换为 UserProfileResponse DTO
     * MapStruct会自动处理大部分同名字段。
     * 对于嵌套的 'user' 字段，它会自动查找下面的 userEntityToUserBasicInfo 方法来完成转换。
     */
    UserProfileResponse toUserProfileResponse(UserProfileEntity entity);


    /**
     * 辅助转换方法：将 UserEntity 转换为 UserProfileResponse.UserBasicInfo
     * 这个方法会被上面的 toUserProfileResponse 自动调用。
     */
    @Mapping(source = "roles", target = "roleNames", qualifiedByName = "rolesToRoleNames")
    UserProfileResponse.UserBasicInfo userEntityToUserBasicInfo(UserEntity userEntity);


    /**
     * 自定义转换逻辑：将 Set<RoleEntity> 转换为 Set<String>
     * 这个方法被上面的 @Mapping 注解通过 qualifiedByName 调用。
     */
    @Named("rolesToRoleNames")
    default Set<String> rolesToRoleNames(Set<RoleEntity> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
    }
}
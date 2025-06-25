package com.kglsys.application.mapper;

import com.kglsys.api.response.RegisterResponse;
import com.kglsys.domain.entity.RoleEntity;
import com.kglsys.domain.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

// componentModel="spring" 让 MapStruct 生成的实现类成为一个Spring Bean
@Mapper(componentModel = "spring")
public interface UserMapper {

    // MapStruct可以自动映射同名字段 (id, username, email 等)
    // 对于 roles 这种需要特殊逻辑的，我们使用 @Mapping 注解来指定
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToRoleNames")
    RegisterResponse toRegisterResponse(UserEntity user);

    // 这是一个命名的方法，被上面的 @Mapping 调用，用于将 RoleEntity 集合转为 String 集合
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
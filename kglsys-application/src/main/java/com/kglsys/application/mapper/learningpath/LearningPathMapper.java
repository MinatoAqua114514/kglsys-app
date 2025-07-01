package com.kglsys.application.mapper.learningpath;

import com.kglsys.domain.learningpath.LearningPath;
import com.kglsys.dto.learningpath.response.LearningPathVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LearningPathMapper {

    LearningPathMapper INSTANCE = Mappers.getMapper(LearningPathMapper.class);

    // MapStruct 自动处理 Enum.name() 到 String 的转换
    @Mapping(source = "difficultyLevel", target = "difficultyLevel")
    LearningPathVo toVo(LearningPath entity);

    List<LearningPathVo> toVoList(List<LearningPath> entities);
}
package com.kglsys.application.mapper;

import com.kglsys.domain.entity.AssessmentQuestion;
import com.kglsys.domain.entity.LearningStyle;
import com.kglsys.domain.entity.QuestionOption;
import com.kglsys.dto.request.AssessmentQuestionRequest;
import com.kglsys.dto.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring") // 关键：让 Spring 管理这个 Mapper Bean
public interface AssessmentMapper {

    AssessmentMapper INSTANCE = Mappers.getMapper(AssessmentMapper.class);

    // --- Admin Mappings ---

    // --- Entity to VO Mappings ---

    @Mapping(source = "id", target = "id") // 即使名称相同，显式写出也是好习惯
    @Mapping(source = "questionText", target = "questionText")
    @Mapping(source = "sequence", target = "sequence")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "options", target = "options") // MapStruct 会自动处理 List<Entity> -> List<VO>
    AssessmentQuestionAdminVo toAdminVo(AssessmentQuestion question);

    List<AssessmentQuestionAdminVo> toAdminVoList(List<AssessmentQuestion> questions);

    @Mapping(source = "contributesToStyle.id", target = "contributesToStyleId")
    @Mapping(source = "contributesToStyle.displayName", target = "contributesToStyleName")
    QuestionOptionAdminVo toAdminVo(QuestionOption option);


    // --- Request DTO to Entity Mappings ---

    // 用于创建新实体
    @Mapping(target = "id", ignore = true) // 创建时忽略 ID
    @Mapping(target = "options", ignore = true) // 选项需要手动处理，因为涉及数据库查询
    AssessmentQuestion toEntity(AssessmentQuestionRequest request);

    // 用于更新已存在的实体
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "options", ignore = true)
    void updateEntityFromRequest(AssessmentQuestionRequest request, @MappingTarget AssessmentQuestion question);


    // =================================================


    // --- Student Mappings (新增) ---

    QuestionnaireQuestionVo toQuestionnaireVo(AssessmentQuestion question);

    List<QuestionnaireQuestionVo> toQuestionnaireVoList(List<AssessmentQuestion> questions);

    QuestionOptionVo toOptionVo(QuestionOption option);

    AssessmentResultVo toAssessmentResultVo(LearningStyle style);

    List<AssessmentResultVo> toAssessmentResultVoList(List<LearningStyle> styles);
}
package com.kglsys.application.mapper;

import com.kglsys.domain.entity.assessment.AssessmentQuestionEntity;
import com.kglsys.domain.entity.assessment.QuestionOptionEntity;
import com.kglsys.api.response.QuestionDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 用于测评相关实体和DTO转换的MapStruct Mapper
 * componentModel="spring" 让 MapStruct 生成的实现类成为一个Spring Bean，可以被注入使用
 */
@Mapper(componentModel = "spring")
public interface AssessmentMapper {

    /**
     * 将 AssessmentQuestion 实体映射到 QuestionDetailResponse DTO。
     * MapStruct 会自动处理以下映射：
     * - question.id -> response.id
     * - question.questionText -> response.questionText
     * - question.options (List<QuestionOption>) -> response.options (List<OptionResponse>)
     * 在映射列表时，它会自动寻找下面的 toOptionResponse 方法进行逐个元素转换。
     *
     * @param question 数据库实体对象
     * @return 用于API响应的DTO对象
     */
    QuestionDetailResponse toQuestionDetailResponse(AssessmentQuestionEntity question);

    /**
     * 将 QuestionOption 实体映射到 QuestionDetailResponse.OptionResponse DTO。
     * 这里的 @Mapping 注解不是必需的，因为字段名完全相同 (id, optionText)，
     * 但写出来可以更清晰地表明映射关系。
     *
     * @param option 数据库实体对象
     * @return 用于API响应的嵌套DTO对象
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "optionText", target = "optionText")
    QuestionDetailResponse.OptionResponse toOptionResponse(QuestionOptionEntity option);

    /**
     * MapStruct 也能自动处理列表映射，我们不需要手动实现这个方法。
     * 它会自动为 List<QuestionOption> 调用 toOptionResponse 方法。
     * 定义这个方法签名可以用于其他需要显式调用的场景。
     */
    List<QuestionDetailResponse.OptionResponse> toOptionResponseList(List<QuestionOptionEntity> options);
}

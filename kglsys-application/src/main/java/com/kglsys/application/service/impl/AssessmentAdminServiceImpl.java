package com.kglsys.application.service.impl;

import com.kglsys.application.mapper.AssessmentMapper; // 引入 Mapper
import com.kglsys.application.service.AssessmentAdminService;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.entity.AssessmentQuestion;
import com.kglsys.domain.entity.LearningStyle;
import com.kglsys.domain.entity.QuestionOption;
import com.kglsys.dto.request.AssessmentQuestionRequest;
import com.kglsys.dto.request.QuestionOptionRequest;
import com.kglsys.dto.response.AssessmentQuestionAdminVo;
import com.kglsys.infra.repository.AssessmentQuestionRepository;
import com.kglsys.infra.repository.LearningStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentAdminServiceImpl implements AssessmentAdminService {

    private final AssessmentQuestionRepository questionRepository;
    private final LearningStyleRepository learningStyleRepository;
    private final AssessmentMapper assessmentMapper; // 注入 Mapper

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentQuestionAdminVo> getAllQuestions() {
        // 使用 Mapper 进行列表转换
        return assessmentMapper.toAdminVoList(questionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentQuestionAdminVo getQuestionById(Integer questionId) {
        AssessmentQuestion question = findQuestionById(questionId);
        // 使用 Mapper 进行单个对象转换
        return assessmentMapper.toAdminVo(question);
    }

    @Override
    @Transactional
    public AssessmentQuestionAdminVo createQuestion(AssessmentQuestionRequest request) {
        // 使用 Mapper 将 DTO 映射到新实体
        AssessmentQuestion question = assessmentMapper.toEntity(request);

        // 手动处理需要业务逻辑的复杂映射
        mapOptionsToQuestion(request, question);

        AssessmentQuestion savedQuestion = questionRepository.save(question);
        return assessmentMapper.toAdminVo(savedQuestion);
    }

    @Override
    @Transactional
    public AssessmentQuestionAdminVo updateQuestion(Integer questionId, AssessmentQuestionRequest request) {
        AssessmentQuestion question = findQuestionById(questionId);

        // 清除旧选项
        question.getOptions().clear();

        // 使用 Mapper 更新实体的基本字段
        assessmentMapper.updateEntityFromRequest(request, question);

        // 手动处理需要业务逻辑的复杂映射
        mapOptionsToQuestion(request, question);

        AssessmentQuestion updatedQuestion = questionRepository.save(question);
        return assessmentMapper.toAdminVo(updatedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestion(Integer questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("无法删除：测评问题未找到，ID: " + questionId);
        }
        questionRepository.deleteById(questionId);
    }

    private AssessmentQuestion findQuestionById(Integer questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("测评问题未找到，ID: " + questionId));
    }

    /**
     * 重构后的辅助方法，只处理包含业务逻辑的复杂映射（查询关联实体）。
     */
    private void mapOptionsToQuestion(AssessmentQuestionRequest request, AssessmentQuestion question) {
        for (QuestionOptionRequest optionRequest : request.getOptions()) {
            LearningStyle style = learningStyleRepository.findById(optionRequest.getContributesToStyleId())
                    .orElseThrow(() -> new ResourceNotFoundException("关联的岗位不存在，ID: " + optionRequest.getContributesToStyleId()));

            QuestionOption option = new QuestionOption();
            option.setOptionText(optionRequest.getOptionText());
            option.setContributesToStyle(style);

            question.addOption(option);
        }
    }
}
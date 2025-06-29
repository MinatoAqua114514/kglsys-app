package com.kglsys.application.service.impl;

import com.kglsys.application.service.AssessmentAdminService;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.entity.AssessmentQuestion;
import com.kglsys.domain.entity.LearningStyle;
import com.kglsys.domain.entity.QuestionOption;
import com.kglsys.dto.request.AssessmentQuestionRequest;
import com.kglsys.dto.request.QuestionOptionRequest;
import com.kglsys.dto.response.AssessmentQuestionAdminVo;
import com.kglsys.dto.response.QuestionOptionAdminVo;
import com.kglsys.infra.repository.AssessmentQuestionRepository;
import com.kglsys.infra.repository.LearningStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentAdminServiceImpl implements AssessmentAdminService {

    private final AssessmentQuestionRepository questionRepository;
    private final LearningStyleRepository learningStyleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentQuestionAdminVo> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::mapQuestionToAdminVo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentQuestionAdminVo getQuestionById(Integer questionId) {
        AssessmentQuestion question = findQuestionById(questionId);
        return mapQuestionToAdminVo(question);
    }

    @Override
    @Transactional
    public AssessmentQuestionAdminVo createQuestion(AssessmentQuestionRequest request) {
        AssessmentQuestion question = new AssessmentQuestion();
        mapRequestToQuestionEntity(request, question);

        AssessmentQuestion savedQuestion = questionRepository.save(question);
        return mapQuestionToAdminVo(savedQuestion);
    }

    @Override
    @Transactional
    public AssessmentQuestionAdminVo updateQuestion(Integer questionId, AssessmentQuestionRequest request) {
        AssessmentQuestion question = findQuestionById(questionId);

        // Clear existing options to replace them. This is simpler and safer than trying to match and update.
        // `orphanRemoval=true` on the entity relationship will ensure the old options are deleted from the DB.
        question.getOptions().clear();

        mapRequestToQuestionEntity(request, question);

        AssessmentQuestion updatedQuestion = questionRepository.save(question);
        return mapQuestionToAdminVo(updatedQuestion);
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
     * Helper method to map DTO data to an entity for both create and update operations.
     */
    private void mapRequestToQuestionEntity(AssessmentQuestionRequest request, AssessmentQuestion question) {
        question.setQuestionText(request.getQuestionText());
        question.setSequence(request.getSequence());
        question.setActive(request.isActive());

        // Map and add new options
        for (QuestionOptionRequest optionRequest : request.getOptions()) {
            LearningStyle style = learningStyleRepository.findById(optionRequest.getContributesToStyleId())
                    .orElseThrow(() -> new ResourceNotFoundException("关联的岗位不存在，ID: " + optionRequest.getContributesToStyleId()));

            QuestionOption option = new QuestionOption();
            option.setOptionText(optionRequest.getOptionText());
            option.setContributesToStyle(style);

            // Set the bidirectional relationship
            question.addOption(option);
        }
    }



    // --- Admin VO Mapping Methods ---

    private AssessmentQuestionAdminVo mapQuestionToAdminVo(AssessmentQuestion question) {
        AssessmentQuestionAdminVo vo = new AssessmentQuestionAdminVo();
        vo.setId(question.getId());
        vo.setQuestionText(question.getQuestionText());
        vo.setSequence(question.getSequence());
        vo.setActive(question.isActive());
        vo.setOptions(question.getOptions().stream()
                .map(this::mapOptionToAdminVo)
                .collect(Collectors.toList()));
        return vo;
    }

    private QuestionOptionAdminVo mapOptionToAdminVo(QuestionOption option) {
        QuestionOptionAdminVo vo = new QuestionOptionAdminVo();
        vo.setId(option.getId());
        vo.setOptionText(option.getOptionText());
        vo.setContributesToStyleId(option.getContributesToStyle().getId());
        vo.setContributesToStyleName(option.getContributesToStyle().getDisplayName());
        return vo;
    }
}
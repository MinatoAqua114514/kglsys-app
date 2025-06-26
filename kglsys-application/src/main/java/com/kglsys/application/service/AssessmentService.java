package com.kglsys.application.service;

import com.kglsys.application.mapper.AssessmentMapper; // 1. 导入 Mapper
//import com.example.yourproject.common.exception.ResourceNotFoundException;
import com.kglsys.common.exception.config.ResourceNotFoundException;
import com.kglsys.domain.entity.assessment.AssessmentQuestionEntity;
import com.kglsys.domain.repository.assessment.AssessmentQuestionRepository;
import com.kglsys.api.response.QuestionDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测评相关功能的业务逻辑层
 */
@Service
public class AssessmentService {

    private final AssessmentQuestionRepository questionRepository;
    private final AssessmentMapper assessmentMapper; // 2. 注入 Mapper

    @Autowired
    public AssessmentService(AssessmentQuestionRepository questionRepository, AssessmentMapper assessmentMapper) {
        this.questionRepository = questionRepository;
        this.assessmentMapper = assessmentMapper; // 3. 初始化 Mapper
    }

    /**
     * 根据ID获取题目详情
     * @param questionId 题目ID
     * @return 包含问题和选项的详细信息DTO
     * @throws ResourceNotFoundException 如果找不到对应ID的题目
     */
    @Transactional(readOnly = true)
    public QuestionDetailResponse getQuestionDetailsById(Integer questionId) {
        AssessmentQuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));

        // 4. 使用 Mapper 进行转换，替换原来的静态方法
        return assessmentMapper.toQuestionDetailResponse(question);
    }
}

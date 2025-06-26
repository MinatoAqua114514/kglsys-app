package com.kglsys.application.service;

import com.kglsys.api.request.AnswerSubmissionRequest;
import com.kglsys.application.mapper.AssessmentMapper; // 1. 导入 Mapper
import com.kglsys.common.exception.config.ResourceNotFoundException;
import com.kglsys.domain.entity.assessment.AssessmentQuestionEntity;
import com.kglsys.domain.entity.assessment.UserAssessmentAnswerEntity;
import com.kglsys.domain.repository.assessment.AssessmentQuestionRepository;
import com.kglsys.api.response.QuestionDetailResponse;
import com.kglsys.domain.repository.assessment.QuestionOptionRepository;
import com.kglsys.domain.repository.assessment.UserAssessmentAnswerRepository;
import com.kglsys.domain.repository.base.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    private final UserAssessmentAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionOptionRepository optionRepository; // 假设存在

    @Autowired
    public AssessmentService(AssessmentQuestionRepository questionRepository, AssessmentMapper assessmentMapper,
                             UserAssessmentAnswerRepository answerRepository, UserRepository userRepository,
                             QuestionOptionRepository optionRepository) {
        this.questionRepository = questionRepository;
        this.assessmentMapper = assessmentMapper; // 3. 初始化 Mapper
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
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

    /**
     * 提交或更新用户的答案
     *
     * @param request 包含用户ID、问题ID和选项ID的请求
     */
    @Transactional // 声明为事务方法，确保数据一致性
    public void submitAnswer(AnswerSubmissionRequest request) {
        // 1. 检查用户、问题、选项是否存在，不存在则抛出异常
        final var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("用户不存在, ID: " + request.getUserId()));

        final var question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("问题不存在, ID: " + request.getQuestionId()));

        final var selectedOption = optionRepository.findById(request.getSelectedOptionId())
                .orElseThrow(() -> new EntityNotFoundException("选项不存在, ID: " + request.getSelectedOptionId()));

        // 2. 根据 user_id 和 question_id 查找是否已存在作答记录
        //    利用我们之前在 Repository 中定义的方法
        UserAssessmentAnswerEntity answer = answerRepository
                .findByUser_IdAndQuestion_Id(request.getUserId(), request.getQuestionId())
                .map(existingAnswer -> {
                    // a. 如果存在，说明用户在修改答案，更新选择的选项
                    existingAnswer.setSelectedOption(selectedOption);
                    return existingAnswer;
                })
                .orElseGet(() -> {
                    // b. 如果不存在，说明是首次答题，创建一个新的实体
                    UserAssessmentAnswerEntity newAnswer = new UserAssessmentAnswerEntity();
                    newAnswer.setUser(user);
                    newAnswer.setQuestion(question);
                    newAnswer.setSelectedOption(selectedOption);
                    // createdAt 字段由 数据库DEFAULT CURRENT_TIMESTAMP COMMENT 自动填充
                    return newAnswer;
                });

        // 3. 保存到数据库（对于新记录是 INSERT，对于已有记录是 UPDATE）
        answerRepository.save(answer);
    }
}

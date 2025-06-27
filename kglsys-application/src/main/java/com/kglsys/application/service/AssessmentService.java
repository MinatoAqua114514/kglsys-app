package com.kglsys.application.service;

import com.kglsys.api.request.AnswerSubmissionRequest;
import com.kglsys.api.request.UpdatePathRequest;
import com.kglsys.api.response.AssessmentResultResponse;
import com.kglsys.api.response.QuestionDetailResponse;
import com.kglsys.application.mapper.AssessmentMapper; // 1. 导入 Mapper
import com.kglsys.common.exception.config.ResourceNotFoundException;
import com.kglsys.domain.entity.assessment.AssessmentQuestionEntity;
import com.kglsys.domain.entity.assessment.LearningStyleEntity;
import com.kglsys.domain.entity.assessment.UserAssessmentAnswerEntity;
import com.kglsys.domain.entity.base.UserEntity;
import com.kglsys.domain.entity.learning.LearningPathEntity;
import com.kglsys.domain.entity.learning.UserLearningProfileEntity;
import com.kglsys.domain.repository.assessment.AssessmentQuestionRepository;
import com.kglsys.domain.repository.assessment.QuestionOptionRepository;
import com.kglsys.domain.repository.assessment.UserAssessmentAnswerRepository;
import com.kglsys.domain.repository.base.UserRepository;
import com.kglsys.domain.repository.learning.LearningPathRepository;
import com.kglsys.domain.repository.learning.UserLearningProfileRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 测评相关功能的业务逻辑层
 */
@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentQuestionRepository questionRepository;
    private final AssessmentMapper assessmentMapper; // 2. 注入 Mapper
    private final UserAssessmentAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionOptionRepository optionRepository;
    private final UserLearningProfileRepository profileRepository;
    private final LearningPathRepository pathRepository;

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

    /**
     * 计算用户的学习风格，查找匹配的学习路径，并更新其学习档案
     * @param userId 需要计算的用户ID
     * @return 包含学习风格和推荐路径列表的响应对象
     */
    @Transactional
    public AssessmentResultResponse calculateAndAssignPaths(Long userId) {
        // 1. 验证用户是否存在，这是最佳实践的第一步
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户 " + userId + " 不存在。"));

        // 2. 获取用户所有答题记录
        List<UserAssessmentAnswerEntity> answers = answerRepository.findAllByUser_Id(userId);
        if (answers.isEmpty()) {
            throw new ResourceNotFoundException("用户 " + userId + " 没有任何答题记录。");
        }

        // 3. 计算主导学习风格
        Optional<LearningStyleEntity> dominantStyleOpt = answers.stream()
                .map(answer -> answer.getSelectedOption().getLearningStyle())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        if (dominantStyleOpt.isEmpty()) {
            // 这个异常在有答案的情况下基本不会触发，但保留是好的
            throw new IllegalStateException("用户 " + userId + " 岗位趋势计算错误，没有满足条件的结果。");
        }
        LearningStyleEntity dominantStyle = dominantStyleOpt.get();

        // 4. 更新或创建用户的学习档案【核心修正部分】
        UserLearningProfileEntity profile = profileRepository.findById(userId)
                .orElse(new UserLearningProfileEntity()); // 查找或新建

        // 正确地建立关联关系
        profile.setUser(user); // 通过设置关联对象，@MapsId 会自动处理主键 userId
        profile.setLearningStyle(dominantStyle);

        // 注意：根据您的 TODO，此处不再设置 assessment_completed_at，这将在用户选择路径后更新。
        profileRepository.save(profile); // 现在JPA能正确识别是INSERT还是UPDATE

        // 5. 根据主导学习风格ID，查找所有相关的学习路径
        // 重点：返回响应结果显示一或多条路径，等待用户自行选择学习路径
        List<LearningPathEntity> recommendedPaths = pathRepository.findByForStyle_Id(dominantStyle.getId());
        if (recommendedPaths.isEmpty()) {
            throw new ResourceNotFoundException("未找到符合当前岗位： " + dominantStyle.getDisplayName() + " 的学习路径");
        }

        // 6. 构建并返回响应对象
        AssessmentResultResponse.LearningStyleDTO styleDTO = new AssessmentResultResponse.LearningStyleDTO(
                dominantStyle.getId(),
                dominantStyle.getDisplayName(),
                dominantStyle.getName()
        );

        List<AssessmentResultResponse.LearningPathDTO> pathDTOs = recommendedPaths.stream()
                .map(path -> new AssessmentResultResponse.LearningPathDTO(path.getId(), path.getName(), path.getDescription()))
                .collect(Collectors.toList());

        return new AssessmentResultResponse(styleDTO, pathDTOs);
    }

    /**
     * 【新增服务方法】
     * 根据请求更新用户的学习路径。
     * @param request 包含用户ID和新的学习路径ID的请求对象
     * @return 如果更新成功返回 true，否则返回 false
     */
    @Transactional
    public boolean updateUserAssignedPath(UpdatePathRequest request) {
        if (request.getUserId() == null || request.getAssignedPathId() == null) {
            throw new IllegalArgumentException("更新用户学习路径的参数不合法");
        }

        // 调用仓库中的自定义更新方法
        int updatedRows = profileRepository.updateAssignedPathIfStyleExists(
                request.getUserId(),
                request.getAssignedPathId(),
                LocalDateTime.now()
        );

        // 如果受影响的行数大于0，说明更新成功
        return updatedRows > 0;
    }
}

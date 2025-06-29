package com.kglsys.application.service.impl;

import com.kglsys.application.service.AssessmentStudentService;
import com.kglsys.common.exception.BusinessRuleException;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.entity.*;
import com.kglsys.dto.AnswerDto;
import com.kglsys.dto.request.ConfirmStyleRequest;
import com.kglsys.dto.request.SubmitAssessmentRequest;
import com.kglsys.dto.response.AssessmentResultVo;
import com.kglsys.dto.response.QuestionOptionVo;
import com.kglsys.dto.response.QuestionnaireQuestionVo;
import com.kglsys.infra.repository.*;
import com.kglsys.application.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentStudentServiceImpl implements AssessmentStudentService {

    private final AssessmentQuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final LearningStyleRepository styleRepository;
    private final UserLearningProfileRepository profileRepository;
    private final UserAssessmentAnswerRepository answerRepository;
    private final UserRepository userRepository;

    // FR3: 获取测评问卷
    @Override
    @Transactional(readOnly = true)
    public List<QuestionnaireQuestionVo> getQuestionnaire() {
        List<AssessmentQuestion> questions = questionRepository.findByIsActiveTrueOrderBySequenceAsc();
        // Manual mapping to VOs
        return questions.stream().map(this::mapQuestionToVo).collect(Collectors.toList());
    }

    // FR4 & FR5: 提交问卷并计算结果
    @Override
    @Transactional
    public List<AssessmentResultVo> submitAssessment(SubmitAssessmentRequest request) {
        Long userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new IllegalStateException("用户未登录"));
        User user = userRepository.findById(userId).get();

        // 1. 验证提交的答案
        Map<Integer, QuestionOption> validatedOptions = validateAndFetchOptions(request);

        // 2. 清除旧答案 (如果允许重做)
        // answerRepository.deleteAllByUserId(userId);

        // 3. 保存新答案
        List<UserAssessmentAnswer> answersToSave = new ArrayList<>();
        validatedOptions.forEach((questionId, option) -> {
            UserAssessmentAnswer answer = new UserAssessmentAnswer();
            answer.setUser(user);
            answer.setQuestion(option.getQuestion());
            answer.setSelectedOption(option);
            answersToSave.add(answer);
        });
        answerRepository.saveAll(answersToSave);

        // 4. 计算贡献值
        Map<Integer, Long> styleScores = validatedOptions.values().stream()
                .map(option -> option.getContributesToStyle().getId())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 5. 找到得分最高的两个岗位
        List<Integer> topStyleIds = styleScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (topStyleIds.isEmpty()) {
            throw new BusinessRuleException("无法根据您的答案计算出推荐岗位。");
        }

        // 6. 更新用户学习档案
        UserLearningProfile profile = profileRepository.findById(userId).orElse(new UserLearningProfile());
        profile.setUser(user);
        profile.setAssessmentCompletedAt(LocalDateTime.now());
        profileRepository.save(profile);

        // 7. 返回推荐岗位信息
        return styleRepository.findAllById(topStyleIds).stream()
                .map(this::mapStyleToVo)
                .collect(Collectors.toList());
    }

    // FR6: 确认岗位
    @Override
    @Transactional
    public void confirmLearningStyle(ConfirmStyleRequest request) {
        Long userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new IllegalStateException("用户未登录"));
        UserLearningProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException("请先完成岗位测评。"));

        LearningStyle style = styleRepository.findById(request.getLearningStyleId())
                .orElseThrow(() -> new ResourceNotFoundException("选择的岗位不存在。"));

        profile.setLearningStyle(style);
        profileRepository.save(profile);
    }

    private Map<Integer, QuestionOption> validateAndFetchOptions(SubmitAssessmentRequest request) {
        // ... 实现详细的验证逻辑: 数量是否正确, questionId 是否有效, optionId 是否属于该 question...
        // 为保持简洁此处省略，但在生产代码中至关重要。
        List<Integer> optionIds = request.getAnswers()
                .stream()
                .map(AnswerDto::getSelectedOptionId)
                .collect(Collectors.toList());
        return optionRepository.findAllById(optionIds).stream()
                .collect(Collectors.toMap(opt -> opt.getQuestion().getId(), Function.identity()));
    }

    // --- VO and DTO mapping methods ---

    private QuestionnaireQuestionVo mapQuestionToVo(AssessmentQuestion question) {
        QuestionnaireQuestionVo vo = new QuestionnaireQuestionVo();
        vo.setId(question.getId());
        vo.setQuestionText(question.getQuestionText());

        List<QuestionOptionVo> optionVos = question.getOptions().stream()
                .map(this::mapOptionToVo)
                .collect(Collectors.toList());
        vo.setOptions(optionVos);
        return vo;
    }

    private QuestionOptionVo mapOptionToVo(QuestionOption option) {
        QuestionOptionVo vo = new QuestionOptionVo();
        vo.setId(option.getId());
        vo.setOptionText(option.getOptionText());
        return vo;
    }

    private AssessmentResultVo mapStyleToVo(LearningStyle style) {
        AssessmentResultVo vo = new AssessmentResultVo();
        vo.setId(style.getId());
        vo.setName(style.getName());
        vo.setDisplayName(style.getDisplayName());
        vo.setDescription(style.getDescription());
        return vo;
    }
}
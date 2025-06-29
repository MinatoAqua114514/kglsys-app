package com.kglsys.application.service;

import com.kglsys.dto.request.ConfirmStyleRequest;
import com.kglsys.dto.request.SubmitAssessmentRequest;
import com.kglsys.dto.response.AssessmentResultVo;
import com.kglsys.dto.response.QuestionnaireQuestionVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AssessmentStudentService {
    // FR3: 获取测评问卷
    List<QuestionnaireQuestionVo> getQuestionnaire();
    // FR4 & FR5: 提交问卷并计算结果
    List<AssessmentResultVo> submitAssessment(SubmitAssessmentRequest request);
    // FR6: 确认岗位
    void confirmLearningStyle(ConfirmStyleRequest request);
}

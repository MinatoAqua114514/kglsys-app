package com.kglsys.web.controller;

import com.kglsys.application.service.AssessmentStudentService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.request.ConfirmStyleRequest;
import com.kglsys.dto.request.SubmitAssessmentRequest;
import com.kglsys.dto.response.AssessmentResultVo;
import com.kglsys.dto.response.QuestionnaireQuestionVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class AssessmentStudentController {

    private final AssessmentStudentService studentService;

    // FR3: 获取问卷
    @GetMapping("/questionnaire")
    public ResponseEntity<ApiResponse<List<QuestionnaireQuestionVo>>> getQuestionnaire() {
        return ResponseEntity.ok(ApiResponse.success(studentService.getQuestionnaire()));
    }

    // FR4 & FR5: 提交问卷
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<List<AssessmentResultVo>>> submitAssessment(@Valid @RequestBody SubmitAssessmentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(studentService.submitAssessment(request)));
    }

    // FR6: 确认岗位
    @PostMapping("/confirm-style")
    public ResponseEntity<ApiResponse<Void>> confirmStyle(@Valid @RequestBody ConfirmStyleRequest request) {
        studentService.confirmLearningStyle(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
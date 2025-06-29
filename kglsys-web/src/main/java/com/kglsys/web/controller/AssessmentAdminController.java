package com.kglsys.web.controller;

import com.kglsys.application.service.AssessmentAdminService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.request.AssessmentQuestionRequest;
import com.kglsys.dto.response.AssessmentQuestionAdminVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/assessment/questions")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AssessmentAdminController {

    private final AssessmentAdminService assessmentAdminService;

    @PostMapping
    public ResponseEntity<ApiResponse<AssessmentQuestionAdminVo>> createQuestion(@Valid @RequestBody AssessmentQuestionRequest request) {
        AssessmentQuestionAdminVo createdQuestion = assessmentAdminService.createQuestion(request);
        return new ResponseEntity<>(ApiResponse.success(createdQuestion), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssessmentQuestionAdminVo>>> getAllQuestions() {
        List<AssessmentQuestionAdminVo> questions = assessmentAdminService.getAllQuestions();
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<AssessmentQuestionAdminVo>> getQuestionById(@PathVariable Integer questionId) {
        AssessmentQuestionAdminVo question = assessmentAdminService.getQuestionById(questionId);
        return ResponseEntity.ok(ApiResponse.success(question));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<ApiResponse<AssessmentQuestionAdminVo>> updateQuestion(@PathVariable Integer questionId, @Valid @RequestBody AssessmentQuestionRequest request) {
        AssessmentQuestionAdminVo updatedQuestion = assessmentAdminService.updateQuestion(questionId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedQuestion));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Integer questionId) {
        assessmentAdminService.deleteQuestion(questionId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
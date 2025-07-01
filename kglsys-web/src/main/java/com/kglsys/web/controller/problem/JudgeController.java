package com.kglsys.web.controller.problem;

import com.kglsys.application.service.problem.JudgeService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.problem.request.CodeSubmissionRequest;
import com.kglsys.dto.problem.response.SubmissionResultVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/judge")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class JudgeController {

    private final JudgeService judgeService;

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmissionResultVo>> submit(@Valid @RequestBody CodeSubmissionRequest request) {
        SubmissionResultVo initialResult = judgeService.submitCode(request);
        return ResponseEntity.accepted().body(ApiResponse.success(initialResult)); // 返回 202 Accepted
    }

    @GetMapping("/submissions/{submissionId}")
    public ResponseEntity<ApiResponse<SubmissionResultVo>> getResult(@PathVariable Long submissionId) {
        SubmissionResultVo result = judgeService.getSubmissionResult(submissionId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
package com.kglsys.web.controller.resource;

import com.kglsys.application.service.resource.ContentService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.resource.response.LearningResourceDetailVo;
import com.kglsys.dto.problem.response.ProblemDetailVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/resources/{resourceId}")
    public ResponseEntity<ApiResponse<LearningResourceDetailVo>> getResourceDetails(@PathVariable Long resourceId) {
        return ResponseEntity.ok(ApiResponse.success(contentService.getLearningResourceDetails(resourceId)));
    }

    @GetMapping("/problems/{problemId}")
    public ResponseEntity<ApiResponse<ProblemDetailVo>> getProblemDetails(@PathVariable Long problemId) {
        return ResponseEntity.ok(ApiResponse.success(contentService.getProblemDetails(problemId)));
    }
}
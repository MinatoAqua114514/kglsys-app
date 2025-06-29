package com.kglsys.web.controller;

import com.kglsys.application.service.LearningPathService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.request.SelectPathRequest;
import com.kglsys.dto.response.LearningPathVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-paths")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class LearningPathController {

    private final LearningPathService learningPathService;

    // FR7: 获取推荐的学习路径
    @GetMapping("/recommended")
    public ResponseEntity<ApiResponse<List<LearningPathVo>>> getRecommendedPaths() {
        List<LearningPathVo> paths = learningPathService.getPathsForCurrentUserStyle();
        return ResponseEntity.ok(ApiResponse.success(paths));
    }

    // FR8: 确定学习路径
    @PostMapping("/select")
    public ResponseEntity<ApiResponse<Void>> selectPath(@Valid @RequestBody SelectPathRequest request) {
        learningPathService.selectLearningPath(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
package com.kglsys.web.controller;

import com.kglsys.application.service.LearningProgressService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.request.UpdateNodeStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class LearningProgressController {

    private final LearningProgressService learningProgressService;

    /**
     * 更新用户对特定学习路径节点的状态。
     * 前端在用户进入节点详情页时，应调用此接口并设置状态为 "IN_PROGRESS"。
     * 当用户点击 "完成" 按钮时，应调用此接口并设置状态为 "MASTERED"。
     *
     * @param nodeId The ID of the node to update.
     * @param request The request body containing the new status.
     * @return A successful response.
     */
    @PutMapping("/nodes/{nodeId}/status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Void>> updateNodeStatus(
            @PathVariable Long nodeId,
            @Valid @RequestBody UpdateNodeStatusRequest request) {

        learningProgressService.updateNodeStatus(nodeId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
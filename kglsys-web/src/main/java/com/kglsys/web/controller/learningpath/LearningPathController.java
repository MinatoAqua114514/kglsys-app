package com.kglsys.web.controller.learningpath;

import com.kglsys.application.service.learningpath.LearningPathService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.learningpath.request.SelectPathRequest;
import com.kglsys.dto.learningpath.response.LearningPathGraphVo;
import com.kglsys.dto.learningpath.response.LearningPathVo;
import com.kglsys.dto.learningpath.response.NodeDetailVo;
import com.kglsys.dto.learningpath.response.NodeKnowledgeGraphVo;
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

    /**
     * 【新增】获取当前用户学习路径的图谱视图。
     * @return 包含节点和边的图谱数据。
     */
    @GetMapping("/me/graph")
    public ResponseEntity<ApiResponse<LearningPathGraphVo>> getMyLearningPathGraph() {
        LearningPathGraphVo graphVo = learningPathService.getLearningPathGraphForCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(graphVo));
    }

    /**
     * 【新增】获取指定学习节点的详细信息，与节点相关联的实体信息构成知识图谱。
     * @param nodeId 节点的ID
     * @return 包含节点所有详细信息的响应。
     */
    @GetMapping("/nodes/{nodeId}")
    public ResponseEntity<ApiResponse<NodeDetailVo>> getNodeDetails(@PathVariable Long nodeId) {
        NodeDetailVo nodeDetail = learningPathService.getNodeDetails(nodeId);
        return ResponseEntity.ok(ApiResponse.success(nodeDetail));
    }

    /**
     * 【新增】获取指定学习节点的知识图谱视图。
     * @param nodeId 节点的ID
     * @return 包含图谱节点和边的响应。
     */
    @GetMapping("/nodes/{nodeId}/knowledge-graph")
    public ResponseEntity<ApiResponse<NodeKnowledgeGraphVo>> getNodeKnowledgeGraph(@PathVariable Long nodeId) {
        NodeKnowledgeGraphVo graphVo = learningPathService.getNodeKnowledgeGraph(nodeId);
        return ResponseEntity.ok(ApiResponse.success(graphVo));
    }
}
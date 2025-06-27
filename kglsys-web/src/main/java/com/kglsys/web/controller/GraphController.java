package com.kglsys.web.controller;

import com.kglsys.api.response.graph.LearningPathGraphDTO;
import com.kglsys.application.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供知识图谱相关数据的API接口
 */
@RestController
@RequestMapping("/api/graph") // 推荐使用版本化的API路径
public class GraphController {

    private final GraphService graphService;
    @Autowired
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    /**
     * 根据学习路径ID获取其对应的知识图谱数据
     *
     * @param pathId 学习路径的ID，从URL路径中获取
     * @return 包含完整节点和边信息的ResponseEntity
     */
    @GetMapping("/learning-path/{pathId}")
    public ResponseEntity<LearningPathGraphDTO> getGraphForLearningPath(@PathVariable Long pathId) {
        LearningPathGraphDTO graphData = graphService.getGraphDataForLearningPath(pathId);
        return ResponseEntity.ok(graphData);
    }
}
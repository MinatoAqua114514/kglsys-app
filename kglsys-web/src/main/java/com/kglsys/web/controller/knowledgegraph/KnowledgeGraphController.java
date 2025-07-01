package com.kglsys.web.controller.knowledgegraph;

import com.kglsys.application.service.knowledgegraph.KnowledgeGraphService;
import com.kglsys.common.responses.ApiResponse;
import com.kglsys.dto.knowledgegraph.response.KgEntityDetailVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kg/entities")
@PreAuthorize("isAuthenticated()") // 任何登录用户都可以查看
@RequiredArgsConstructor
public class KnowledgeGraphController {

    private final KnowledgeGraphService knowledgeGraphService;

    /**
     * 获取知识实体的详细信息。
     * @param entityId 实体的ID
     * @return 包含实体所有详情的响应。
     */
    @GetMapping("/{entityId}")
    public ResponseEntity<ApiResponse<KgEntityDetailVo>> getEntityDetails(@PathVariable Long entityId) {
        KgEntityDetailVo entityDetail = knowledgeGraphService.getEntityDetails(entityId);
        return ResponseEntity.ok(ApiResponse.success(entityDetail));
    }
}
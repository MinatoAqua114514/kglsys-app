package com.kglsys.api.response.graph;

import com.kglsys.domain.entity.graph.ContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 知识节点的数据传输对象 (用于API响应)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeNodeDTO {
    private Long id;
    private String name;
    private String description;
    private ContentType contentType;
    // 注意：这里我们不包含 content_url 或 problem_id，
    // 因为图谱展示时主要关心节点本身。具体资源可以在用户点击节点后再次请求。
}
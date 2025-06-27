package com.kglsys.domain.entity.graph;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 知识图谱边（关系）实体类
 * 映射 'knowledge_graph_edges' 表
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "knowledge_graph_edges", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"source_node_id", "target_node_id"}, name = "uk_source_target")
})
public class KnowledgeGraphEdgeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 关系类型 (如: PREREQUISITE 前置, RELATED 相关)
     */
    @Column(name = "relation_type", nullable = false, length = 50)
    private String relationType;

    // --- 关系定义 ---

    /**
     * 多对一关系：多条边可以有同一个起始节点
     * 使用 LAZY 延迟加载，提高性能
     * 使用 @ToString.Exclude 避免在 toString() 中产生循环依赖
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_node_id", nullable = false)
    @ToString.Exclude
    private KnowledgeNodeEntity sourceNode;

    /**
     * 多对一关系：多条边可以有同一个目标节点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_node_id", nullable = false)
    @ToString.Exclude
    private KnowledgeNodeEntity targetNode;

    // --- 正确的 equals 和 hashCode 实现 ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeGraphEdgeEntity that = (KnowledgeGraphEdgeEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
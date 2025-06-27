package com.kglsys.domain.entity.graph;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 知识图谱节点实体类
 * 映射 'knowledge_nodes' 表
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "knowledge_nodes")
public class KnowledgeNodeEntity implements Serializable {

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
     * 知识点名称 (如: Java基础语法, 哈希表)
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * 知识点的详细描述/学习目标
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 主要内容类型 (文章, 视频, 编程题)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;

    /**
     * 学习资源链接 (文章或视频URL)
     */
    @Column(name = "content_url", length = 512)
    private String contentUrl;

    /**
     * 若类型为PROBLEM, 关联的编程题ID
     * 注意: 这里只存储ID，完整的关联关系需要一个 ProblemEntity 实体类
     */
    @Column(name = "problem_id")
    private Long problemId;

    /**
     * 记录创建时间
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 记录更新时间
     */
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // --- 关系定义 ---

    /**
     * 一个知识点可以作为多条边的起始节点
     * mappedBy 指向 KnowledgeGraphEdgeEntity 中的 'sourceNode' 字段
     * cascade = CascadeType.ALL: 当删除知识点时，级联删除所有以此为起点的边
     * orphanRemoval = true: 当从这个集合中移除边时，自动删除数据库中对应的记录
     * 使用 @ToString.Exclude 避免在 toString() 中产生循环依赖
     */
    @OneToMany(mappedBy = "sourceNode", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<KnowledgeGraphEdgeEntity> outgoingEdges = new HashSet<>();

    /**
     * 一个知识点可以作为多条边的目标节点
     * mappedBy 指向 KnowledgeGraphEdgeEntity 中的 'targetNode' 字段
     */
    @OneToMany(mappedBy = "targetNode", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<KnowledgeGraphEdgeEntity> incomingEdges = new HashSet<>();

    // --- 正确的 equals 和 hashCode 实现 ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeNodeEntity that = (KnowledgeNodeEntity) o;
        // 只有当 id 不为 null 时才比较 id，否则认为是不同的对象
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // 使用 getClass().hashCode() 保证了代理对象和实际对象的 hashCode 一致性
        // 对于未持久化的对象（id 为 null），它们将具有不同的哈希码
        return getClass().hashCode();
    }
}
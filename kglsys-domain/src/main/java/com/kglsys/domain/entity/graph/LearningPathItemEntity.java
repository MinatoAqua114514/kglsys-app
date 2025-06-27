package com.kglsys.domain.entity.graph;

import com.kglsys.domain.entity.learning.LearningPathEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 学习路径-知识节点关联实体类
 * 映射 'learning_path_items' 表，作为学习路径和知识节点的中间表
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "learning_path_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"path_id", "node_id"}, name = "uk_path_node")
})
public class LearningPathItemEntity implements Serializable {
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
     * 知识节点在此路径中的学习顺序
     */
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    // --- 关系定义 ---

    /**
     * 多对一关系：多个路径项可以属于同一个学习路径
     * 使用 @ToString.Exclude 避免在 toString() 中产生循环依赖
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id", nullable = false)
    @ToString.Exclude
    private LearningPathEntity path;

    /**
     * 多对一关系：多个路径项可以关联同一个知识节点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    @ToString.Exclude
    private KnowledgeNodeEntity node;

    // --- 正确的 equals 和 hashCode 实现 ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearningPathItemEntity that = (LearningPathItemEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
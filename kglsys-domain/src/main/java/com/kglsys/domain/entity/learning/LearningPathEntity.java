package com.kglsys.domain.entity.learning;

import com.kglsys.domain.entity.assessment.LearningStyleEntity;

import com.kglsys.domain.entity.graph.LearningPathItemEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 预设学习路径实体类
 * 映射 'learning_paths' 表
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "learning_paths")
public class LearningPathEntity implements Serializable {

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
     * 学习路径名称 (如: Java后端入门路径)
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * 路径描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 多对一关系：多个学习路径可以适配同一种学习风格
     * 使用 LAZY 延迟加载，只有在需要时才加载关联的 LearningStyleEntity
     * 使用 @ToString.Exclude 避免在 toString() 中产生循环依赖
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "for_style_id")
    @ToString.Exclude
    private LearningStyleEntity forStyle;

    // --- 新增关系 ---

    /**
     * 一对多关系：一个学习路径包含多个知识节点项 (LearningPathItemEntity)
     * mappedBy 指向 LearningPathItemEntity 中的 'path' 字段
     * cascade = CascadeType.ALL: 当删除学习路径时，级联删除其下的所有路径项
     * orphanRemoval = true: 确保从 items 集合中移除的项也会从数据库中删除
     * 为了在查询时能按顺序获取知识点，建议在 Service/Repository 层使用 @OrderBy("sequence ASC") 或 JPQL 查询。
     * 在实体中直接使用 @OrderBy 会对所有查询生效，可能影响灵活性。
     * 使用 @ToString.Exclude 避免在 toString() 中产生循环依赖
     */
    @OneToMany(
            mappedBy = "path",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private List<LearningPathItemEntity> items = new ArrayList<>();

    // --- 正确的 equals 和 hashCode 实现 ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearningPathEntity that = (LearningPathEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
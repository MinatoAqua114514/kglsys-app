package com.kglsys.domain.entity;

import com.kglsys.domain.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 学习路径定义实体，映射到 `learning_paths` 表。
 */
@Entity
@Table(name = "learning_paths")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "for_style_id")
    private LearningStyle learningStyle;

    /**
     * 路径包含的节点列表。
     * @OrderBy: 保证从数据库获取时节点总是有序的。
     */
    @OneToMany(mappedBy = "learningPath", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sequence ASC")
    @Builder.Default
    private List<LearningPathNode> nodes = new ArrayList<>();

    // --- 关系管理辅助方法 ---
    public void addNode(LearningPathNode node) {
        this.nodes.add(node);
        node.setLearningPath(this);
    }

    public void removeNode(LearningPathNode node) {
        this.nodes.remove(node);
        node.setLearningPath(null);
    }
}
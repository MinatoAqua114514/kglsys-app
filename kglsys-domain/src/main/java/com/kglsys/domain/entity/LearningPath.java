package com.kglsys.domain.entity;

import com.kglsys.domain.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 学习路径定义实体
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
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    // 关联适配的岗位
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "for_style_id")
    private LearningStyle learningStyle;

    // 一个学习路径包含多个有序的节点
    @OneToMany(mappedBy = "learningPath", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sequence ASC") // 保证节点按顺序获取
    private List<LearningPathNode> nodes;
}
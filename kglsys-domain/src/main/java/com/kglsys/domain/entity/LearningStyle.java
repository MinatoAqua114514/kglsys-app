package com.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

/**
 * 软件工程专业岗位定义实体 (原 learning_styles 表)
 */
@Entity
@Table(name = "learning_styles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Lob // 使用 @Lob 来映射 TEXT 类型
    @Column(name = "description")
    private String description;

    // 一个岗位可以对应多个学习路径
    @OneToMany(mappedBy = "forStyle", fetch = FetchType.LAZY)
    private Set<LearningPath> learningPaths;
}
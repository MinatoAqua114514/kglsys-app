package com.kglsys.domain.entity.assessment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 学习风格定义实体类
 * 映射 'learning_styles' 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "learning_styles")
public class LearningStyleEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 学习风格的英文标识 (如: backend_developer)
     * 具有唯一约束
     */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /**
     * 学习风格的中文名称 (如: 后端开发工程师)
     */
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    /**
     * 该风格的详细描述
     */
    @Lob // 使用 @Lob 来映射 TEXT 类型
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 一对多关系：一个学习风格可以被多个问题选项所指向
     * mappedBy 指向 QuestionOption 实体中的 'learningStyle' 字段
     * 使用 @JsonIgnore 防止在序列化时产生无限循环
     */
    @JsonIgnore
    @OneToMany(mappedBy = "learningStyle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionOptionEntity> questionOptions;
}

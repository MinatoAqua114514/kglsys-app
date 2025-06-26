package com.kglsys.domain.entity.assessment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 测评问题选项实体类
 * 映射 'question_options' 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question_options")
public class QuestionOptionEntity implements Serializable {

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
     * 选项的文本内容
     */
    @Column(name = "option_text", nullable = false, length = 512)
    private String optionText;

    /**
     * 多对一关系：多个选项属于同一个问题
     * 使用 @JsonIgnore 防止在序列化时产生无限循环
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private AssessmentQuestionEntity question;

    /**
     * 多对一关系：多个选项可以贡献于同一种学习风格
     * 这里不需要 @JsonIgnore，因为通常我们希望在获取选项时看到它贡献的学习风格
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contributes_to_style_id", nullable = false)
    private LearningStyleEntity learningStyle;
}

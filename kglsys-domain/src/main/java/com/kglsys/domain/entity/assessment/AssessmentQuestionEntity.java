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
 * 测评问卷问题实体类
 * 映射 'assessment_questions' 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assessment_questions")
public class AssessmentQuestionEntity implements Serializable {

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
     * 问题内容
     */
    @Column(name = "question_text", nullable = false, length = 512)
    private String questionText;

    /**
     * 问题显示的顺序
     */
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    /**
     * 是否启用该问题
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * 一对多关系：一个问题拥有多个选项
     * mappedBy 指向 QuestionOption 实体中的 'question' 字段
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<QuestionOptionEntity> options;

    /**
     * 一对多关系：一个问题可以被多个用户回答
     * mappedBy 指向 UserAssessmentAnswer 实体中的 'question' 字段
     */
    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserAssessmentAnswerEntity> userAnswers;
}

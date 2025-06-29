package com.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * 测评问卷问题实体，映射到 `assessment_questions` 表。
 */
@Getter
@Setter
@Entity
@Table(name = "assessment_questions")
public class AssessmentQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "question_text", nullable = false, length = 512)
    private String questionText;

    @Column(nullable = false)
    private Integer sequence;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    /**
     * 问题包含的选项列表。
     * orphanRemoval=true: 当一个选项从这个list中移除时，它将从数据库中被删除。
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestionOption> options = new ArrayList<>();

    /**
     * 关系管理辅助方法。
     * 添加一个选项，并维护双向关联。
     * @param option 要添加的选项。
     */
    public void addOption(QuestionOption option) {
        this.options.add(option);
        option.setQuestion(this);
    }

    /**
     * 关系管理辅助方法。
     * 移除一个选项。
     * @param option 要移除的选项。
     */
    public void removeOption(QuestionOption option) {
        this.options.remove(option);
        option.setQuestion(null);
    }
}
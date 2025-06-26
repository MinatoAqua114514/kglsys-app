package com.kglsys.domain.entity.assessment;

import com.kglsys.domain.entity.base.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户测评答案记录实体类
 * 映射 'user_assessment_answers' 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_assessment_answers",
        uniqueConstraints = {
                // 定义数据库层面的联合唯一约束，对应 uk_user_question
                @UniqueConstraint(columnNames = {"user_id", "question_id"}, name = "uk_user_question")
        })
public class UserAssessmentAnswerEntity implements Serializable {

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
     * 关联的用户 (多对一)
     * 假设你有一个 User 实体类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * 关联的问题 (多对一)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AssessmentQuestionEntity question;

    /**
     * 用户选择的选项 (多对一)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id", nullable = false)
    private QuestionOptionEntity selectedOption;

    /**
     * 答题时间
     * 使用 @CreationTimestamp 会在实体创建时自动填充当前时间
     * updatable = false 确保该字段在更新时不会被改变
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

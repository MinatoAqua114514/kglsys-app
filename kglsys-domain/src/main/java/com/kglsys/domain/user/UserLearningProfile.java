package com.kglsys.domain.user;

import com.kglsys.domain.learningpath.LearningPath;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 用户学习档案实体，映射到 `user_learning_profile` 表。
 * 记录用户的测评结果（岗位）和选择的学习路径。
 */
@Getter
@Setter
@Entity
@Table(name = "user_learning_profile")
public class UserLearningProfile {

    /**
     * 主键，与 User 的主键共享。
     */
    @Id
    private Long userId;

    /**
     * 指向 User 的一对一关联。
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 用户测评后确定的目标岗位 (LearningStyle)。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_style_id")
    private LearningStyle learningStyle;

    /**
     * 用户选择的学习路径。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_path_id")
    private LearningPath learningPath;

    @Column(name = "assessment_completed_at")
    private LocalDateTime assessmentCompletedAt;
}
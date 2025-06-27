package com.kglsys.domain.entity.learning;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kglsys.domain.entity.assessment.LearningStyleEntity;
import com.kglsys.domain.entity.base.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户学习档案实体类（包含用户的学习风格和指定的学习路径）
 * 映射 'user_learning_profile' 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_learning_profile")
public class UserLearningProfileEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，与用户ID关联
     * 注意：这里不是自增的，它的值来自于 users 表的主键
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 多对一关系：多个用户档案可以有相同的学习风格
     * 使用 EAGER 立即加载，因为获取用户档案时通常也需要其学习风格信息
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "learning_style_id") // 对应外键 learning_style_id
    private LearningStyleEntity learningStyle;

    /**
     * 多对一关系：多个用户档案可以被分配相同的学习路径
     * 使用 EAGER 立即加载，因为获取用户档案时通常也需要其指定的学习路径
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_path_id") // 对应外键 assigned_path_id
    private LearningPathEntity assignedPath;

    /**
     * 测评完成时间
     */
    @Column(name = "assessment_completed_at")
    private LocalDateTime assessmentCompletedAt;


    /**
     * 一对一关系：学习档案属于一个用户
     * @MapsId: 表示该实体的ID（userId）是由 'user' 这个关联关系映射的，共享主键。
     * @JoinColumn: 指定外键列。
     * @JsonIgnore: 防止在将此实体序列化为JSON时，因双向关系导致无限循环。
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;
}

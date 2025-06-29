package com.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_learning_profile")
public class UserLearningProfile {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_style_id")
    private LearningStyle learningStyle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_path_id")
    private LearningPath learningPath;

    @Column(name = "assessment_completed_at")
    private LocalDateTime assessmentCompletedAt;
}
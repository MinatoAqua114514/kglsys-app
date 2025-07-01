package com.kglsys.domain.assessment;

import com.kglsys.domain.user.LearningStyle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "question_options")
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "option_text", nullable = false, length = 512)
    private String optionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AssessmentQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contributes_to_style_id", nullable = false)
    private LearningStyle contributesToStyle;
}
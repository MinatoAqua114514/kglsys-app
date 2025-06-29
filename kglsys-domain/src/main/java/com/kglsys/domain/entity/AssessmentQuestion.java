package com.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options = new ArrayList<>();

    // Add this helper method to AssessmentQuestion entity for cleaner code
    public void addOption(QuestionOption option) {
        this.options.add(option);
        option.setQuestion(this);
    }
}
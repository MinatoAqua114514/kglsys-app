package com.kglsys.domain.problem;

import com.kglsys.domain.problem.enums.SubmissionStatus;
import com.kglsys.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Lob
    @Column(name = "source_code", nullable = false)
    private String sourceCode;

    @Column(nullable = false, length = 50)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;

    @Column(name = "execution_time_ms")
    private Integer executionTimeMs;

    @Column(name = "memory_used_kb")
    private Integer memoryUsedKb;

    @Lob
    @Column(name = "judge_output")
    private String judgeOutput; // For compiler errors or other messages

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onPrePersist() {
        submittedAt = LocalDateTime.now();
    }
}
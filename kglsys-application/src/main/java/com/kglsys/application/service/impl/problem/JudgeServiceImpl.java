package com.kglsys.application.service.impl.problem;

import com.kglsys.application.mapper.resource.ContentMapper;
import com.kglsys.application.service.problem.JudgeService;
import com.kglsys.application.util.SecurityUtil;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.problem.Problem;
import com.kglsys.domain.problem.Submission;
import com.kglsys.domain.user.User;
import com.kglsys.domain.problem.enums.SubmissionStatus;
import com.kglsys.dto.problem.request.CodeSubmissionRequest;
import com.kglsys.dto.problem.response.SubmissionResultVo;
import com.kglsys.infra.repository.problem.ProblemRepository;
import com.kglsys.infra.repository.problem.SubmissionRepository;
import com.kglsys.infra.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {

    private static final Logger logger = LoggerFactory.getLogger(JudgeServiceImpl.class);

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final ContentMapper contentMapper;

    @Override
    @Transactional
    public SubmissionResultVo submitCode(CodeSubmissionRequest request) {
        Long userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new IllegalStateException("用户未登录"));
        User user = userRepository.getReferenceById(userId);
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new ResourceNotFoundException("题目不存在，ID: " + request.getProblemId()));

        Submission submission = Submission.builder()
                .user(user)
                .problem(problem)
                .sourceCode(request.getSourceCode())
                .language(request.getLanguage())
                .status(SubmissionStatus.PENDING) // 初始状态为排队中
                .build();

        Submission savedSubmission = submissionRepository.save(submission);

        // 触发异步评测
        processSubmission(savedSubmission.getId());

        return contentMapper.toSubmissionResultVo(savedSubmission);
    }

    @Override
    @Transactional(readOnly = true)
    public SubmissionResultVo getSubmissionResult(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("提交记录不存在，ID: " + submissionId));
        return contentMapper.toSubmissionResultVo(submission);
    }

    @Async("taskExecutor") // 指定使用我们配置的线程池
    @Transactional
    public void processSubmission(Long submissionId) {
        // 在新的事务中获取 submission
        Submission submission = submissionRepository.findById(submissionId).orElse(null);
        if (submission == null) {
            logger.error("无法处理评测任务：找不到提交记录，ID: {}", submissionId);
            return;
        }

        try {
            // 1. 更新状态为 JUDGING
            submission.setStatus(SubmissionStatus.JUDGING);
            submissionRepository.save(submission);
            logger.info("开始评测提交 ID: {}", submissionId);

            // 模拟评测过程
            Thread.sleep(5000); // 模拟耗时

            // =================================================================
            // 【安全警告】: 此处是评测核心的占位符
            // 真实生产环境中，绝不能直接在服务器上执行用户代码！
            // 必须使用安全的沙箱环境（如 Docker）来隔离执行过程。
            //
            // 概念流程:
            // 1. 创建一个临时的 Docker 容器。
            // 2. 将用户的 sourceCode 和题目的测试用例复制到容器中。
            // 3. 在容器内编译代码。如果失败，记录编译错误，更新状态为 COMPILE_ERROR。
            // 4. 依次用测试用例运行编译后的程序，设置好时间和内存限制。
            // 5. 捕获程序的输出，与标准答案对比。
            // 6. 如果超时/超内存，更新状态为 TLE/MLE。
            // 7. 如果输出不匹配，更新状态为 WRONG_ANSWER。
            // 8. 如果所有用例通过，更新状态为 ACCEPTED。
            // 9. 无论结果如何，最后都要销毁容器。
            // =================================================================

            // 模拟一个随机的评测结果
            double random = Math.random();
            if (random < 0.7) {
                submission.setStatus(SubmissionStatus.ACCEPTED);
                submission.setExecutionTimeMs(120);
                submission.setMemoryUsedKb(2048);
            } else if (random < 0.9) {
                submission.setStatus(SubmissionStatus.WRONG_ANSWER);
                submission.setJudgeOutput("On test case 3, expected '42', but got '41'");
            } else {
                submission.setStatus(SubmissionStatus.TIME_LIMIT_EXCEEDED);
            }

            submissionRepository.save(submission);
            logger.info("评测完成，提交 ID: {}, 结果: {}", submissionId, submission.getStatus());

        } catch (Exception e) {
            logger.error("评测提交 ID: {} 时发生未知错误", submissionId, e);
            // 异常处理，可以设置一个 FAILED 状态
        }
    }
}
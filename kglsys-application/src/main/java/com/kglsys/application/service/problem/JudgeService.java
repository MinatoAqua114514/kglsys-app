package com.kglsys.application.service.problem;

import com.kglsys.dto.problem.request.CodeSubmissionRequest;
import com.kglsys.dto.problem.response.SubmissionResultVo;

public interface JudgeService {
    /**
     * 提交代码进行评测。
     * @param request 包含代码、语言和题目ID的请求。
     * @return 一个包含 submissionId 的结果对象，初始状态为 PENDING。
     */
    SubmissionResultVo submitCode(CodeSubmissionRequest request);

    /**
     * 获取指定提交的评测结果。
     * @param submissionId 提交ID。
     * @return 包含最新状态和结果的视图对象。
     */
    SubmissionResultVo getSubmissionResult(Long submissionId);
}
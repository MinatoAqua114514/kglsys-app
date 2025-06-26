package com.kglsys.web.controller;

import com.kglsys.common.util.ApiResponse;
import com.kglsys.application.service.AssessmentService;
import com.kglsys.api.response.QuestionDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assessment") // 为此控制器下的所有接口设置统一的前缀
public class AssessmentController {

    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    /**
     * API接口：通过ID获取单个题目的详细信息
     * GET /api/assessment/questions/{id}
     *
     * @param id 题目ID，从URL路径中获取
     * @return 包含题目详情的统一响应体
     */
    @GetMapping("/questions/{id}")
    public ApiResponse<QuestionDetailResponse> getQuestionById(@PathVariable Integer id) {
        // 调用Service层获取数据
        QuestionDetailResponse questionDetails = assessmentService.getQuestionDetailsById(id);
        // 使用ApiResponse.success()包装成功响应
        return ApiResponse.success(questionDetails);
    }
}

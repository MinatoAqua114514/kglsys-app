package com.kglsys.web.controller;

import com.kglsys.api.request.AnswerSubmissionRequest;
import com.kglsys.api.request.UpdatePathRequest;
import com.kglsys.api.response.AssessmentResultResponse;
import com.kglsys.common.util.ApiResponse;
import com.kglsys.application.service.AssessmentService;
import com.kglsys.api.response.QuestionDetailResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 接收并保存用户的答题记录
     *
     * @param request 包含用户ID、问题ID和选项ID的请求体
     * @return 返回一个统一的响应结果
     */
    @PostMapping("/answer")
    public ApiResponse<Void> submitAnswer(@Valid @RequestBody AnswerSubmissionRequest request) {
        try {
            assessmentService.submitAnswer(request);
            return ApiResponse.success("答题成功");
        } catch (EntityNotFoundException e) {
            // 在这里简单处理Service层抛出的实体未找到异常
            // 更好的做法是使用 @ControllerAdvice 定义全局异常处理器
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他未知异常
            // 在生产环境中，应该记录详细的错误日志
            // log.error("提交答案时发生未知错误", e);
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误，请稍后重试");
        }
    }

    /**
     * 提交测评并计算学习风格及推荐路径的API接口
     * @param userId 从URL路径中获取的用户ID
     * @return 返回一个包含详细测评结果的JSON对象，或在无法计算时返回404
     */
    @PostMapping("/submit/{userId}")
    public ApiResponse<AssessmentResultResponse> submitAssessment(@PathVariable Long userId) {
        // 调用更新后的服务方法
        AssessmentResultResponse result = assessmentService.calculateAndAssignPaths(userId);

        if (result != null) {
            return ApiResponse.success("用户趋势岗位计算成功", result);
        } else {
            // 如果用户没有答案或发生其他错误，返回 "Not Found"
            return ApiResponse.error(404, "测评结果计算错误");
        }
    }

    /**
     * 【新增API接口】
     * 手动为用户指派或更新学习路径。
     * @param request 包含userId和assignedPathId的JSON请求体
     * @return 成功则返回成功信息，失败则返回400错误及原因
     */
    @PostMapping("/assign-path")
    public ApiResponse<?> assignLearningPath(@RequestBody UpdatePathRequest request) {
        boolean success = assessmentService.updateUserAssignedPath(request);
        if (success) {
            return ApiResponse.success("学习路径更新成功。");
        } else {
            return ApiResponse.error(400, "更新失败。请确认用户ID正确，且该用户已完成学习风格测评。");
        }
    }
}

package com.kglsys.web.controller;

import com.kglsys.api.request.AnswerSubmissionRequest;
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
}

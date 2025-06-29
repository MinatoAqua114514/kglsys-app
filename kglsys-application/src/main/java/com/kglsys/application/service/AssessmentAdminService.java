package com.kglsys.application.service;

import com.kglsys.dto.request.AssessmentQuestionRequest;
import com.kglsys.dto.response.AssessmentQuestionAdminVo;

import java.util.List;

public interface AssessmentAdminService {
    /**
     * Retrieves all assessment questions with their options.
     * @return A list of question VOs.
     */
    List<AssessmentQuestionAdminVo> getAllQuestions();

    /**
     * Retrieves a single assessment question by its ID.
     * @param questionId The ID of the question.
     * @return The question VO.
     */
    AssessmentQuestionAdminVo getQuestionById(Integer questionId);

    /**
     * Creates a new assessment question along with its options.
     * @param request The DTO containing question and option data.
     * @return The created question VO.
     */
    AssessmentQuestionAdminVo createQuestion(AssessmentQuestionRequest request);

    /**
     * Updates an existing assessment question and its options.
     * Options are replaced entirely with the new set provided.
     * @param questionId The ID of the question to update.
     * @param request The DTO with updated data.
     * @return The updated question VO.
     */
    AssessmentQuestionAdminVo updateQuestion(Integer questionId, AssessmentQuestionRequest request);

    /**
     * Deletes an assessment question and its associated options.
     * @param questionId The ID of the question to delete.
     */
    void deleteQuestion(Integer questionId);
}
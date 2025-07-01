package com.kglsys.application.service.resource;

import com.kglsys.dto.resource.response.LearningResourceDetailVo;
import com.kglsys.dto.problem.response.ProblemDetailVo;

public interface ContentService {
    LearningResourceDetailVo getLearningResourceDetails(Long resourceId);
    ProblemDetailVo getProblemDetails(Long problemId);
}
package com.kglsys.application.service;

import com.kglsys.dto.request.SelectPathRequest;
import com.kglsys.dto.response.LearningPathVo;

import java.util.List;

public interface LearningPathService {
    List<LearningPathVo> getPathsForCurrentUserStyle();
    void selectLearningPath(SelectPathRequest request);
}

package com.kglsys.application.service.impl.resource;

import com.kglsys.application.mapper.resource.ContentMapper;
import com.kglsys.application.service.resource.ContentService;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.resource.LearningResource;
import com.kglsys.domain.problem.Problem;
import com.kglsys.dto.resource.response.LearningResourceDetailVo;
import com.kglsys.dto.problem.response.ProblemDetailVo;
import com.kglsys.infra.repository.resource.LearningResourceRepository;
import com.kglsys.infra.repository.problem.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final LearningResourceRepository resourceRepository;
    private final ProblemRepository problemRepository;
    private final ContentMapper contentMapper;

    @Override
    @Transactional(readOnly = true)
    public LearningResourceDetailVo getLearningResourceDetails(Long resourceId) {
        LearningResource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("学习资源未找到，ID: " + resourceId));
        return contentMapper.toResourceDetailVo(resource);
    }

    @Override
    @Transactional(readOnly = true)
    public ProblemDetailVo getProblemDetails(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("编程题目未找到，ID: " + problemId));
        return contentMapper.toProblemDetailVo(problem);
    }
}

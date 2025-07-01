package com.kglsys.application.mapper.resource;

import com.kglsys.domain.resource.LearningResource;
import com.kglsys.domain.problem.Problem;
import com.kglsys.domain.problem.Submission;
import com.kglsys.dto.resource.response.LearningResourceDetailVo;
import com.kglsys.dto.problem.response.ProblemDetailVo;
import com.kglsys.dto.problem.response.SubmissionResultVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    LearningResourceDetailVo toResourceDetailVo(LearningResource resource);

    ProblemDetailVo toProblemDetailVo(Problem problem);

    @Mapping(source = "id", target = "id")
    SubmissionResultVo toSubmissionResultVo(Submission submission);
}
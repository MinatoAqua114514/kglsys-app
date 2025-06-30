package com.kglsys.application.mapper;

import com.kglsys.domain.entity.LearningPath;
import com.kglsys.domain.enums.LearningStatus;
import com.kglsys.dto.response.GraphEdgeVo;
import com.kglsys.dto.response.GraphNodeVo;
import com.kglsys.dto.response.LearningPathGraphVo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public abstract class LearningPathGraphMapperDecorator implements LearningPathGraphMapper {

    // 将delegate字段改为非final
    protected LearningPathGraphMapper delegate;

    // 使用Setter注入,Spring会在创建完Bean之后,调用这个方法来注入依赖
    @Autowired
    @Qualifier("delegate")
    public void setDelegate(LearningPathGraphMapper delegate) {
        this.delegate = delegate;
    }


    @Override
    public LearningPathGraphVo toGraphVo(LearningPath path, Map<Long, LearningStatus> progressMap) {
        // 通过 this.delegate 调用到原始的、由MapStruct生成的实现
        LearningPathGraphVo vo = delegate.toGraphVo(path, progressMap);

        if (path == null || path.getNodes() == null) {
            vo.setNodes(Collections.emptyList());
            vo.setEdges(Collections.emptyList());
            return vo;
        }

        List<GraphNodeVo> nodes = path.getNodes().stream()
                .map(node -> toGraphNodeVo(node, progressMap))
                .distinct()
                .collect(Collectors.toList());
        vo.setNodes(nodes);

        List<GraphEdgeVo> edges = path.getNodes().stream()
                .flatMap(node -> node.getDependencies() != null ? node.getDependencies().stream() : Stream.empty())
                .distinct()
                .map(this::toGraphEdgeVo)
                .collect(Collectors.toList());
        vo.setEdges(edges);

        return vo;
    }
}
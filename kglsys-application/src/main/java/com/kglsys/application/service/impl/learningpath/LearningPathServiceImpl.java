package com.kglsys.application.service.impl.learningpath;

import com.kglsys.application.mapper.knowledgegraph.KnowledgeGraphMapper;
import com.kglsys.application.mapper.learningpath.LearningPathGraphMapper;
import com.kglsys.application.mapper.learningpath.LearningPathMapper;
import com.kglsys.application.mapper.learningpath.NodeDetailMapper;
import com.kglsys.application.service.learningpath.LearningPathService;
import com.kglsys.common.exception.BusinessRuleException;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.learningpath.enums.LearningStatus;
import com.kglsys.domain.knowledgegraph.KgEntity;
import com.kglsys.domain.knowledgegraph.KgRelationship;
import com.kglsys.domain.learningpath.LearningPath;
import com.kglsys.domain.learningpath.LearningPathNode;
import com.kglsys.domain.learningpath.PathNodeEntity;
import com.kglsys.domain.user.UserLearningProfile;
import com.kglsys.domain.user.UserLearningProgress;
import com.kglsys.dto.learningpath.request.SelectPathRequest;
import com.kglsys.dto.learningpath.response.LearningPathGraphVo;
import com.kglsys.dto.learningpath.response.LearningPathVo;
import com.kglsys.dto.learningpath.response.NodeDetailVo;
import com.kglsys.dto.learningpath.response.NodeKnowledgeGraphVo;
import com.kglsys.application.util.SecurityUtil;
import com.kglsys.infra.repository.knowledgegraph.KgRelationshipRepository;
import com.kglsys.infra.repository.learningpath.LearningPathNodeRepository;
import com.kglsys.infra.repository.learningpath.LearningPathRepository;
import com.kglsys.infra.repository.learningpath.PathNodeEntityRepository;
import com.kglsys.infra.repository.user.UserLearningProfileRepository;
import com.kglsys.infra.repository.user.UserLearningProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LearningPathServiceImpl implements LearningPathService {

    private final LearningPathRepository pathRepository;
    private final UserLearningProfileRepository profileRepository;
    private final UserLearningProgressRepository progressRepository;
    private final LearningPathMapper learningPathMapper;
    private final LearningPathGraphMapper graphMapper;
    private final LearningPathNodeRepository nodeRepository;
    private final PathNodeEntityRepository pathNodeEntityRepository;
    private final NodeDetailMapper nodeDetailMapper;
    private final KgRelationshipRepository relationshipRepository;
    private final KnowledgeGraphMapper knowledgeGraphMapper;

    // FR7: 根据已确认岗位列出路径
    @Override
    @Transactional(readOnly = true)
    public List<LearningPathVo> getPathsForCurrentUserStyle() {
        Long userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new IllegalStateException("用户未登录"));
        // 使用新方法，一次查询就加载了 profile 和 style
        UserLearningProfile profile = profileRepository.findWithStyleByUserId(userId)
                .orElseThrow(() -> new BusinessRuleException("请先完成岗位测评。"));

        if (profile.getLearningStyle() == null) {
            throw new BusinessRuleException("请先确认您的目标岗位。");
        }

        Integer styleId = profile.getLearningStyle().getId();
        List<LearningPath> paths = pathRepository.findByForStyle_Id(styleId);

        // 使用 Mapper 转换
        return learningPathMapper.toVoList(paths);
    }

    // FR8: 选择学习路径
    @Override
    @Transactional
    public void selectLearningPath(SelectPathRequest request) {
        Long userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new IllegalStateException("用户未登录"));
        UserLearningProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException("用户档案不存在。"));

        LearningPath path = pathRepository.findById(request.getPathId())
                .orElseThrow(() -> new ResourceNotFoundException("选择的学习路径不存在。"));

        // 校验该路径是否与用户选择的岗位匹配
        if (!path.getForStyle().getId().equals(profile.getLearningStyle().getId())) {
            throw new BusinessRuleException("该学习路径不适用于您当前选择的岗位。");
        }

        profile.setLearningPath(path);
        profileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public LearningPathGraphVo getLearningPathGraphForCurrentUser() {
        // 1. 获取当前用户ID
        Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("无法获取当前用户信息"));

        // 2. 使用优化后的查询获取用户档案及其学习路径
        UserLearningProfile profile = profileRepository.findWithLearningPathByUserId(userId)
                .orElseThrow(() -> new BusinessRuleException("请先完成岗位测评并选择一个学习路径。"));

        if (profile.getLearningPath() == null) {
            throw new BusinessRuleException("您尚未选择任何学习路径。");
        }
        Long pathId = profile.getLearningPath().getId();

        // 3. 使用优化后的查询，一次性获取路径、所有节点及节点间的所有依赖关系
        LearningPath path = pathRepository.findWithAllDependenciesById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("学习路径未找到，ID: " + pathId));

        // 4. 获取该路径下所有节点的ID集合
        Set<Long> nodeIds = path.getNodes().stream()
                .map(LearningPathNode::getId)
                .collect(Collectors.toSet());

        // 5. 一次性查询用户在这些节点上的所有学习进度
        Map<Long, LearningStatus> progressMap = progressRepository.findByUser_IdAndPathNode_IdIn(userId, nodeIds)
                .stream()
                .collect(Collectors.toMap(
                        progress -> progress.getPathNode().getId(),
                        UserLearningProgress::getStatus
                ));

        // 6. 使用 Mapper 将所有聚合的数据转换为最终的 VO
        return graphMapper.toGraphVo(path, progressMap);
    }

    @Override
    @Transactional(readOnly = true)
    public NodeDetailVo getNodeDetails(Long nodeId) {
        // 1. 获取当前用户ID
        Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("无法获取当前用户信息"));

        // 2. 使用优化查询获取节点及其所有邻居
        LearningPathNode node = nodeRepository.findWithDependenciesById(nodeId)
                .orElseThrow(() -> new ResourceNotFoundException("学习节点未找到，ID: " + nodeId));

        // 3. 获取用户对该节点的学习状态
        LearningStatus status = progressRepository.findByUser_IdAndPathNode_Id(userId, nodeId)
                .map(UserLearningProgress::getStatus)
                .orElse(LearningStatus.NOT_STARTED);

        // 4. 使用优化查询，一次性获取节点关联的实体、资源、问题
        List<PathNodeEntity> pathNodeEntities = pathNodeEntityRepository.findByPathNode_Id(nodeId);

        // 5. 使用 Mapper 将所有聚合的数据转换为最终的 VO
        return nodeDetailMapper.toDetailVo(node, status, pathNodeEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public NodeKnowledgeGraphVo getNodeKnowledgeGraph(Long nodeId) {
        // 1. 验证节点是否存在
        if (!nodeRepository.existsById(nodeId)) {
            throw new ResourceNotFoundException("学习节点未找到，ID: " + nodeId);
        }

        // 2. 获取与该节点关联的所有实体
        List<KgEntity> entities = pathNodeEntityRepository.findByPathNode_Id(nodeId)
                .stream()
                .map(PathNodeEntity::getEntity)
                .distinct()
                .toList();

        if (entities.isEmpty()) {
            return NodeKnowledgeGraphVo.builder().nodes(List.of()).edges(List.of()).build();
        }

        // 3. 提取实体ID集合
        Set<Long> entityIds = entities.stream().map(KgEntity::getId).collect(Collectors.toSet());

        // 4. 查询这些实体之间的所有内部关系
        List<KgRelationship> relationships = relationshipRepository.findBySourceEntity_IdInAndTargetEntity_IdIn(entityIds, entityIds);

        // 5. 使用 Mapper 进行转换
        return NodeKnowledgeGraphVo.builder()
                .nodes(knowledgeGraphMapper.toGraphNodeVoList(entities))
                .edges(knowledgeGraphMapper.toGraphEdgeVoList(relationships))
                .build();
    }
}
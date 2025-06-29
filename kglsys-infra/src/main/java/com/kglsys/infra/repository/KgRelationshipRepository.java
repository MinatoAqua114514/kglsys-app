package com.kglsys.infra.repository;

import com.kglsys.domain.entity.KgRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KgRelationshipRepository extends JpaRepository<KgRelationship, Long> {

    /**
     * 查找从某个实体出发的所有关系。
     * @param sourceEntityId 源实体的数据库ID
     * @return 关系列表
     */
    List<KgRelationship> findBySourceEntityId(Long sourceEntityId);

    /**
     * 查找指向某个实体的所有关系。
     * @param targetEntityId 目标实体的数据库ID
     * @return 关系列表
     */
    List<KgRelationship> findByTargetEntityId(Long targetEntityId);

    /**
     * 查找特定类型的关系。
     * @param relationType 关系类型 (e.g., "实现")
     * @return 关系列表
     */
    List<KgRelationship> findByRelationType(String relationType);
}
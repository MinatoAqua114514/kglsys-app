package com.kglsys.infra.repository;

import com.kglsys.domain.entity.KgEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KgEntityRepository extends JpaRepository<KgEntity, Long> {

    /**
     * 根据唯一的实体ID查找实体，这是最核心的查询方法。
     * @param entityId 业务唯一标识符 (e.g., "java_language")
     * @return Optional<KgEntity>
     */
    Optional<KgEntity> findByEntityId(String entityId);

    /**
     * 根据实体类型查找实体列表。
     * @param type 实体类型 (e.g., "Framework")
     * @return 实体列表
     */
    List<KgEntity> findByType(String type);

    /**
     * 模糊查询，用于实现搜索功能。
     * @param name 实体名称的部分或全部
     * @return 匹配的实体列表
     */
    List<KgEntity> findByNameContainingIgnoreCase(String name);

    /**
     * 使用 @EntityGraph 解决 N+1 查询问题。
     * 当查询一个实体，并需要立即访问其关联的 'properties' 集合时，此方法会通过一次 JOIN 查询将属性一并加载。
     * @param entityId 业务唯一标识符
     * @return 包含属性的实体 Optional
     */
    @EntityGraph(attributePaths = {"properties"})
    Optional<KgEntity> findWithPropertiesByEntityId(String entityId);
}
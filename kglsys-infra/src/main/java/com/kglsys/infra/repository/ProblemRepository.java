package com.kglsys.infra.repository;

import com.kglsys.domain.entity.Problem;
import com.kglsys.domain.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    /**
     * 根据难度查找题目。
     * @param difficulty 难度枚举
     * @return 题目列表
     */
    List<Problem> findByDifficulty(Difficulty difficulty);

    /**
     * 根据标题进行模糊搜索。
     * @param title 标题关键字
     * @return 题目列表
     */
    List<Problem> findByTitleContainingIgnoreCase(String title);

    /**
     * 根据标签进行查询。由于tags是JSON类型，需要使用原生SQL查询。
     * @param tag 要搜索的标签
     * @return 包含该标签的题目列表
     */
    @Query(value = "SELECT * FROM problems WHERE JSON_CONTAINS(tags, CAST(:tag AS JSON))", nativeQuery = true)
    List<Problem> findByTag(@Param("tag") String tag);
}
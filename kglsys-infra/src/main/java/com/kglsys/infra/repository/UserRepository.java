package com.kglsys.infra.repository;

import com.kglsys.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * User 实体的 JPA Repository。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户。
     * 这是 Spring Security 进行认证时需要的核心方法。
     * @param username 用户名
     * @return 包含用户的 Optional，如果不存在则为空
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查指定用户名的用户是否存在。
     * 这比 findByUsername().isPresent() 更高效，因为它通常会转换为 COUNT SQL 查询。
     * @param username 用户名
     * @return 如果存在则返回 true，否则返回 false
     */
    Boolean existsByUsername(String username);

    /**
     * 检查指定邮箱的用户是否存在。
     * @param email 邮箱地址
     * @return 如果存在则返回 true，否则返回 false
     */
    Boolean existsByEmail(String email);
}
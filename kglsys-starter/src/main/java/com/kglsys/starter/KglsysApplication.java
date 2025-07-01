package com.kglsys.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Spring Boot 应用启动类。
 * 这是整个应用的入口点。
 * <p>
 * 由于采用了多模块架构，必须显式配置扫描路径，否则Spring Boot只会扫描本模块（starter）下的组件。
 */
@SpringBootApplication
// 扫描所有模块中以 "com.kglsys" 开头的包，以发现 @Component, @Service, @RestController 等 Bean。
@ComponentScan(basePackages = {"com.kglsys"})
// *** 显式启用JPA仓库，并指定扫描的基础包路径 ***
// // 扫描 infra 模块中的 JPA Repository 接口。
@EnableJpaRepositories(basePackages = {"com.kglsys.infra.repository"})
// 扫描 domain 模块中的 JPA 实体类
@EntityScan(basePackages = {"com.kglsys.domain"})
public class KglsysApplication {

    public static void main(String[] args) {
        SpringApplication.run(KglsysApplication.class, args);
    }

}

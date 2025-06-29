package com.kglsys.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// 显式地告诉Spring扫描所有以 "com.kglsys" 开头的包
// 这样就能找到所有模块中的 @RestController, @Service, @Repository, @Configuration 等Bean
@ComponentScan(basePackages = {"com.kglsys"})
// *** 新增：显式启用JPA仓库，并指定扫描的基础包路径 ***
// 这个路径应该指向你所有Repository接口所在的包
@EnableJpaRepositories(basePackages = {"com.kglsys.infra.repository"})
// 可选，但建议添加以明确指定：扫描实体类所在的包
@EntityScan(basePackages = {"com.kglsys.domain.entity"})
public class KglsysApplication {

    public static void main(String[] args) {
        SpringApplication.run(KglsysApplication.class, args);
    }

}

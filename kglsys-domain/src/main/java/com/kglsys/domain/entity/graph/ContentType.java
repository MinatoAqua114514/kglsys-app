package com.kglsys.domain.entity.graph;

/**
 * 知识点主要内容类型的枚举
 * 对应 knowledge_nodes 表中的 content_type 字段
 */
public enum ContentType {
    /**
     * 文章类型
     */
    ARTICLE,

    /**
     * 视频类型
     */
    VIDEO,

    /**
     * 编程题类型
     */
    PROBLEM
}
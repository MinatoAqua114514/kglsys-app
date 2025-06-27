package com.kglsys.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于用户自行选择学习路径
 * 手动更新学习路径的请求类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePathRequest {
    private Long userId;
    private Long assignedPathId;
}
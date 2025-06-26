package com.kglsys.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回给前端的题目详情响应类
 */
@Data
@NoArgsConstructor
public class QuestionDetailResponse {

    private Integer id;
    private String questionText;
    private List<OptionResponse> options;

    /**
     * 内部静态类，用于表示选项信息
     */
    @Data
    @NoArgsConstructor
    public static class OptionResponse {
        private Integer id;
        private String optionText;
    }
}
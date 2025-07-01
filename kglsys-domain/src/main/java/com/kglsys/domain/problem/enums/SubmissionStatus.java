package com.kglsys.domain.problem.enums;

public enum SubmissionStatus {
    PENDING,      // 排队中
    JUDGING,      // 正在评测
    ACCEPTED,     // 通过
    WRONG_ANSWER, // 答案错误
    TIME_LIMIT_EXCEEDED, // 超时
    MEMORY_LIMIT_EXCEEDED, // 超内存
    COMPILE_ERROR, // 编译错误
    RUNTIME_ERROR // 运行错误
}
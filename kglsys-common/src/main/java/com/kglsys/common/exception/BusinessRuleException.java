package com.kglsys.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 表示违反业务规则的异常。
 * 例如，学生用户尝试设置职称，或提交不符合逻辑的数据。
 * 触发此异常通常返回 HTTP 400 Bad Request。
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessRuleException extends RuntimeException {
  public BusinessRuleException(String message) {
    super(message);
  }
}
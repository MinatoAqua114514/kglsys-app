package com.kglsys.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 表示违反业务规则的异常，例如不符合用户角色逻辑等。
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
public class BusinessRuleException extends RuntimeException {
  public BusinessRuleException(String message) {
    super(message);
  }
}
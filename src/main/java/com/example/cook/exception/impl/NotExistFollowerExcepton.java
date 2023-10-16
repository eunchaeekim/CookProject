package com.example.cook.exception.impl;

import com.example.cook.exception.AbstractException;
import org.springframework.http.HttpStatus;


public class NotExistFollowerExcepton extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "팔로우 대상 사용자를 찾을 수 없습니다.";
  }
}


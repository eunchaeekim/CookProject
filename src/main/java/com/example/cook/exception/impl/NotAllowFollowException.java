package com.example.cook.exception.impl;

import com.example.cook.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotAllowFollowException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "자기 자신을 팔로우할 수 없습니다.";
  }
}


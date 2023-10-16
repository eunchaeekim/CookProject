package com.example.cook.exception.impl;

import com.example.cook.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistPostException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "포스팅 글이 존재하지 않습니다";
  }
}


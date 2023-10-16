package com.example.cook.exception.impl;

import com.example.cook.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OnlyWirterException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "작성자만 삭제할 수 있습니다.";
  }
}

package com.example.cook.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice// 예외 처리를 담당하는 클래스, 컨트롤러 레이어와 가까운 위치에 있으며, 지정된 예외가 발생하면 해당 예외를 처리하고 클라이언트에게 응답을 반환
public class CustomExceptionHandler {
  @ExceptionHandler(AbstractException.class)
  protected ResponseEntity<?> handleCustomException(AbstractException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(e.getStatusCode())
        .message(e.getMessage())
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
  }
}
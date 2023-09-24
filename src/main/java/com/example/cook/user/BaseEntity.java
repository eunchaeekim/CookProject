package com.example.cook.user;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

   /*
    nullable: 이 컬럼이 null 값을 허용하는지를 결정
    updatable: 이 컬럼이 업데이트 가능한지를 결정
    columnDefinition: 이 컬럼의 데이터 타입 및 기본값을 정의
     */

  @CreatedDate
  @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(nullable = true)
  private LocalDateTime updatedDate;
}

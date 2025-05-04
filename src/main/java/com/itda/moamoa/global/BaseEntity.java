package com.itda.moamoa.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)  // Entity Life Cycle Event을 Listen
@Getter
@MappedSuperclass                               // Entity 아닌 BaseEntity를 Entity들이 상속할 수 있도록
public abstract class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private boolean deleteFlag = false;

    //soft delete 수행
    public void softDelete(){
        this.deletedAt = LocalDateTime.now();
        this.deleteFlag = true;
    }

    //soft delete 복구
    public void restore(){
        this.deletedAt = null;
        this.deleteFlag = false;
    }

    //삭제 여부 확인
    public boolean isDeleted(){
        return deleteFlag;
    }

}
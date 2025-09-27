package com.choongang.studyreservesystem.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	@CreatedDate
	@Column(updatable = false, name = "created_at")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@Column(updatable = true, name = "updated_at")
	private LocalDateTime updatedAt;
	@Column(columnDefinition = "boolean default false")
	private boolean deleted;
}
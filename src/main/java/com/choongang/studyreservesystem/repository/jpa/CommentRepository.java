package com.choongang.studyreservesystem.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choongang.studyreservesystem.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

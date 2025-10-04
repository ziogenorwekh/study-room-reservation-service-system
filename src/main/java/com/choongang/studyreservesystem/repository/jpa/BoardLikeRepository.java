package com.choongang.studyreservesystem.repository.jpa;

import com.choongang.studyreservesystem.domain.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findByBoardPostIdAndUserId(Long postId, Long userId);
    long countByBoardPostId(Long postId);
    void deleteByBoardPostIdAndUserId(Long postId, Long userId);
}
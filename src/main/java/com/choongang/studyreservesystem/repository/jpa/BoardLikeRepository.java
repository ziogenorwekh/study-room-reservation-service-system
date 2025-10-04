package com.choongang.studyreservesystem.repository.jpa;

import com.choongang.studyreservesystem.domain.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findByBoardBoardIdAndUserId(Long boardId, Long userId);
    long countByBoardBoardId(Long boardId);
    void deleteByBoardBoardIdAndUserId(Long boardId, Long userId);
}
package com.choongang.studyreservesystem.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choongang.studyreservesystem.domain.Board;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUsername(String username);
    List<Board> findByTitleContaining(String keyword);
}

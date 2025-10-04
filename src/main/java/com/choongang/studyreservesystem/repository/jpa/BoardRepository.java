package com.choongang.studyreservesystem.repository.jpa;

import com.choongang.studyreservesystem.dto.CreatePostDto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.choongang.studyreservesystem.domain.Board;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUsername(String username);
    List<Board> findByTitleContaining(String keyword);


    // 작성자 삭제시, 일괄 null 처리 (유저 삭제 전에 호출)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Board b SET b.author = NULL WHERE b.author.id = :userId")
    int detachAuthorByUserId(@Param("userId") Long userId);

    // 권한 체크 최적화용 (엔티티 전체 로딩 없이 authorId만 확인)
    @Query("SELECT b.author.id FROM Board b WHERE b.id = :boardId")
    Optional<Long> findAuthorIdByBoardId(@Param("boardId") Long boardId);

}

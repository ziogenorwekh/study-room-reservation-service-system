package com.choongang.studyreservesystem;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.exception.BoardNotFoundException;
import com.choongang.studyreservesystem.exception.UnauthorizedDeleteException;
import com.choongang.studyreservesystem.repository.jpa.BoardRepository;
import com.choongang.studyreservesystem.service.jpa.BoardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudyReserveSystemApplicationTests {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void testPostDelete1_NotFound() {
        Assertions.assertThrows(BoardNotFoundException.class, () -> {
            boardService.deletePost(1L, "testUser", "ROLE_USER");
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 권한 없음")
    void testPostDelete2_Unauthorized() {
        Board board = Board.builder()
                .username("testUser")
                .title("testTitle")
                .content("testContent")
                .build();
        Board savedBoard = boardRepository.save(board);

        Assertions.assertThrows(UnauthorizedDeleteException.class, () -> {
            boardService.deletePost(savedBoard.getBoardId(), "otherUser", "ROLE_USER");
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 게시글 작성자 삭제 성공")
    void testPostDelete3_UserSuccess() {
        Board board = Board.builder()
                .username("testUser")
                .title("testTitle")
                .content("testContent")
                .build();
        Board savedBoard = boardRepository.save(board);

        Assertions.assertDoesNotThrow(() -> {
            boardService.deletePost(savedBoard.getBoardId(), "testUser", "ROLE_USER");
        });

        Assertions.assertFalse(boardRepository.existsById(savedBoard.getBoardId()));
    }

    @Test
    @DisplayName("게시글 삭제 - 관리자 삭제 성공")
    void testPostDelete4_AdminSuccess() {
        Board board = Board.builder()
                .username("testUser")
                .title("testTitle")
                .content("testContent")
                .build();
        Board savedBoard = boardRepository.save(board);

        Assertions.assertDoesNotThrow(() -> {
            boardService.deletePost(savedBoard.getBoardId(), "admin", "ROLE_ADMIN");
        });

        Assertions.assertFalse(boardRepository.existsById(savedBoard.getBoardId()));
    }
}

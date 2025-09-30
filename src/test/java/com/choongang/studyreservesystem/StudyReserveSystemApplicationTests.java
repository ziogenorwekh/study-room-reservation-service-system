package com.choongang.studyreservesystem;

import com.choongang.studyreservesystem.domain.Board;
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

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void testPostDelete1() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            boardService.delete(1L);
        });
    }


}

package com.choongang.studyreservesystem.service.jpa;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.repository.jpa.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Long createPost(CreatePostDto createPostDto) {
        Board board = Board.builder()
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .username(createPostDto.getUsername())
                .createdAt(LocalDateTime.now())
                .likeCount(0L)
                .build();
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getPostId();
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        boardRepository.delete(board);
    }
}
package com.choongang.studyreservesystem.service.jpa;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.repository.jpa.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Long createPost(CreatePostDto createPostDto) {
        Board board = Board.builder()
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .username(createPostDto.getUsername())
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

    // 게시글 목록 조회
    public List<Board> getAllPosts() {
        return boardRepository.findAll();
    }

    // 게시글 상세 조회 (자기 게시글이거나 관리자일 경우 조회수 증가 없음 구현하자)
    @Transactional
    public Board getPostByPostId(Long id) {
        Board post = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        post.increaseViewCount();
        return boardRepository.save(post);
    }

}
package com.choongang.studyreservesystem.service.jpa;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.dto.CreatePostDto;

import java.util.List;

public interface BoardService {
    void createPost(CreatePostDto createPostDto);
    void deletePost(Long boardId, String currentUsername, String userRole);
    List<Board> getAllPosts();
    Board getPostByPostId(Long id);
}

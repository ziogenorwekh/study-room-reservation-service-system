package com.choongang.studyreservesystem.service.jpa;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.dto.UpdatePostDto;

import java.util.List;

public interface BoardService {
    void createPost(CreatePostDto createPostDto);
    void updatePost(Long boardId, Long actorUserId, UpdatePostDto updatePostDto);
    void deletePost(Long boardId, String currentUsername, String userRole);
    List<Board> getAllPosts();
    Board getPostByPostId(Long id);
    boolean isAuthor(Long boardId, Long userId);

}

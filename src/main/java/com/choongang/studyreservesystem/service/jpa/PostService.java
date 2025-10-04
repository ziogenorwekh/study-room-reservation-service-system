package com.choongang.studyreservesystem.service.jpa;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.dto.SearchPostDto;
import com.choongang.studyreservesystem.dto.UpdatePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    void createPost(CreatePostDto createPostDto);
    void updatePost(Long boardId, Long actorUserId, UpdatePostDto updatePostDto);
    void deletePost(Long boardId, String currentUsername, String userRole);
    void toggleLike(Long boardId, Long userId);
    Board getPostByPostId(Long id);
    boolean isAuthor(Long boardId, Long userId);
    Page<Board> getAllPosts(Pageable pageable);
    Page<Board> searchPosts(SearchPostDto searchPostDto, Pageable pageable);

}

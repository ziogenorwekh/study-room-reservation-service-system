package com.choongang.studyreservesystem.controller;

import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.service.jpa.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/create")
    public String showCreateForm(Model model) {
        model.addAttribute("createPostDto", new CreatePostDto());
        return "createPostForm";
    }

    // 게시글 등록 POST
    @PostMapping("/board/create")
    public String createPost(@ModelAttribute CreatePostDto createPostDto) {
        boardService.createPost(createPostDto);
        return "redirect:/board/list";
    }

    // 게시글 삭제
    @DeleteMapping("/board/post/{postId}")
    public String deletePost(@PathVariable Long postId) {
        boardService.delete(postId);
        return "redirect:/board/list";
    }
}

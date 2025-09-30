package com.choongang.studyreservesystem.controller;

import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.service.jpa.BoardService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class BoardController {
    @GetMapping("/board/create")
    public String showCreateForm(Model model) {
        model.addAttribute("createPostDto", new CreatePostDto());
        return "createPostForm";
    }

    // 게시글 등록 POST
    @PostMapping("/board/create")
    public String createPost(@ModelAttribute CreatePostDto createPostDto, @ModelAttribute BoardService boardService) {
        boardService.createPost(createPostDto);
        return "redirect:/board/list";
    }
}

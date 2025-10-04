package com.choongang.studyreservesystem.controller;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.domain.User;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.dto.UpdatePostDto;
import com.choongang.studyreservesystem.security.CustomUserDetails;
import com.choongang.studyreservesystem.service.jpa.BoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/create")
    public String showCreateForm(Model model) {
        model.addAttribute("createPostDto", new CreatePostDto());
        return "board/createPostForm";
    }

    // 게시글 등록 POST
    @PostMapping("/board/create")
    public String createPost(@ModelAttribute CreatePostDto createPostDto) {
        boardService.createPost(createPostDto);
        return "redirect:/board/list";
    }

    // 게시글 목록 조회
    @GetMapping("/board/list")
    public String boardList(Model model) {
        model.addAttribute("posts", boardService.getAllPosts());
        return "board/boardList";
    }

    // 게시글 상세 조회
    @GetMapping("/board/post/{postId}")
    public String postDetail(@PathVariable Long postId, Model model) {
        Board post = boardService.getPostByPostId(postId);
        model.addAttribute("post", post);
        return "board/postDetail";
    }

    // 게시글 삭제
    @DeleteMapping("/board/post/{boardId}")
    public String deleteBoard(@PathVariable Long boardId,
                              @AuthenticationPrincipal UserDetails userDetails) {
        String currentUsername = userDetails.getUsername();
        String userRole = userDetails.getAuthorities().stream().findFirst()
                                     .map(GrantedAuthority::getAuthority)
                                     .orElse("ROLE_USER");

        boardService.deletePost(boardId, currentUsername, userRole);
        return "redirect:/board/list";
    }

    // 게시글 수정
    @PostMapping("/board/post/{id}/edit")
    public String editBoard(@PathVariable Long id,
                            @Valid @ModelAttribute("form") UpdatePostDto updatePostDto,
                            BindingResult br,
                            @AuthenticationPrincipal CustomUserDetails user,
                            RedirectAttributes ra,
                            Model model) {
        // 폼 검증 실패 → 수정 폼으로
        if (br.hasErrors()) {
            model.addAttribute("boardId", id);
            return "board/updateBoardForm";
        }

        // 인증 누락 방어
        if (user == null) {
            ra.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        try {
            String actorUsername = user.getUsername();
            boardService.updatePost(id, user.getId(), updatePostDto);

        } catch (SecurityException se) {
            ra.addFlashAttribute("error", "작성자 본인만 수정할 수 있습니다.");
            return "redirect:/board/post/" + id;

        } catch (org.springframework.dao.OptimisticLockingFailureException oe) {
            ra.addFlashAttribute("error", "다른 사용자가 먼저 수정했습니다. 최신 내용을 확인한 뒤 다시 저장해주세요.");
            return "redirect:/board/post/" + id + "/edit";

        } catch (IllegalArgumentException ie) {
            ra.addFlashAttribute("error", "게시글을 찾을 수 없습니다.");
            return "redirect:/board/list";
        }

        ra.addFlashAttribute("message", "게시글을 수정했습니다.");
        return "redirect:/board/post/" + id;
    }
}

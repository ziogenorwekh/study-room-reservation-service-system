package com.choongang.studyreservesystem.controller;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.domain.User;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.dto.UpdatePostDto;
import com.choongang.studyreservesystem.security.CustomUserDetails;
import com.choongang.studyreservesystem.service.jpa.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.choongang.studyreservesystem.dto.SearchPostDto;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;

    @GetMapping("/board/create")
    public String showCreateForm(Model model) {
        model.addAttribute("createPostDto", new CreatePostDto());
        return "board/createPostForm";
    }

    // 게시글 등록 POST
    @PostMapping("/board/create")
    public String createPost(@ModelAttribute CreatePostDto createPostDto,
                             Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        createPostDto.setUsername(authentication.getName());

        postService.createPost(createPostDto);
        return "redirect:/board/list";
    }

    // 게시글 목록 조회
    @GetMapping("/board/list")
    public String boardList(
            @RequestParam(defaultValue = "0", name="page") int page,
            @RequestParam(required = false, name="keyword") String keyword,
            @RequestParam(defaultValue = "all", name="searchType") String searchType,
            Model model) {

        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Board> boardPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            SearchPostDto searchDto = SearchPostDto.builder()
                .keyword(keyword)
                .searchType(searchType)
                .build();
            boardPage = postService.searchPosts(searchDto, pageable);
        } else {
            boardPage = postService.getAllPosts(pageable);
        }

        model.addAttribute("posts", boardPage.getContent());
        model.addAttribute("pageInfo", boardPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        return "board/boardList";
    }

    // 게시글 상세 조회
    @GetMapping("/board/post/{postId}")
    public String postDetail(@PathVariable Long postId, Model model) {
        Board post = postService.getPostByPostId(postId);
        model.addAttribute("post", post);
        return "board/postDetail";
    }



    // 게시글 삭제
    @PostMapping("/board/post/{postId}")
    public String deleteBoard(@PathVariable Long postId,
                              @AuthenticationPrincipal UserDetails userDetails) {
        String currentUsername = userDetails.getUsername();
        String userRole = userDetails.getAuthorities().stream().findFirst()
                                     .map(GrantedAuthority::getAuthority)
                                     .orElse("ROLE_USER");

        postService.deletePost(postId, currentUsername, userRole);
        return "redirect:/board/list";
    }

    // 게시글 좋아요 토글
    @PostMapping("/board/post/{postId}/like")
    @ResponseBody
    public String toggleLike(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            return "login_required";
        }
        postService.toggleLike(postId, user.getId());
        return "success";
    }

    // 게시글 수정 Form (Get)
    @GetMapping("/board/post/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal CustomUserDetails me) {
        Board board = postService.getPostByPostId(id); // ✅ 여기서도 인스턴스 호출

        boolean owner = board.getUsername().equals(me.getUsername());
        boolean admin = me.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!(owner || admin)) throw new AccessDeniedException("권한 없음");

        model.addAttribute("form", UpdatePostDto.from(board));
        return "board/edit";
    }

    // 게시글 수정 저장(Post)
    @PostMapping("/board/post/{id}/edit")
    public String editBoard(@PathVariable Long id,
                            @Valid @ModelAttribute("form") UpdatePostDto updatePostDto,
                            BindingResult br,
                            @AuthenticationPrincipal CustomUserDetails user,
                            RedirectAttributes ra,
                            Model model) {
        // 폼 검증 실패 → 수정 폼으로
        if (br.hasErrors()) {
            model.addAttribute("postId", id);
            return "board/updateBoardForm";
        }

        // 인증 누락 방어
        if (user == null) {
            ra.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        try {
            String actorUsername = user.getUsername();
            postService.updatePost(id, user.getId(), updatePostDto);

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

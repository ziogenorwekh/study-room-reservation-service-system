package com.choongang.studyreservesystem.service.jpa.Impl;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.dto.UpdatePostDto;
import com.choongang.studyreservesystem.exception.BoardDeletionException;
import com.choongang.studyreservesystem.exception.BoardNotFoundException;
import com.choongang.studyreservesystem.exception.UnauthorizedDeleteException;
import com.choongang.studyreservesystem.repository.jpa.BoardRepository;
import com.choongang.studyreservesystem.service.jpa.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    // HHE: createBoard는 말이 안됩니다. 이 메서드는 보드를 생성하는 것이 아니라 개별 포스트를 생성하는 것이기 때문입니다.
    // 메서드명의 변경을 원하는 것이 아니라, 엔티티명이 변경되길 원하시면 다시 말씀해주시길 바랍니다.
    @Transactional
    @Override
    public void createPost(CreatePostDto createPostDto) {
        Board board = Board.builder()
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .username(createPostDto.getUsername())
                .likeCount(0L)
                .build();
        Board savedBoard = boardRepository.save(board);
    }



    // 게시글이 없다는 것은 BoardNotFoundException 이라는 네이밍을 가진 클래스를 사용하면 좋을 것 같아요.
    // 지금 쓰신 예외는 인수 입력 처리에 발생하는 에러입니다.
    // 저 예외처리를 받아서 핸들링 하는 다른 클래스와 혼동되는 문제가 생겨요 -> 예) 해당 예외처리하는 메서드는 400으로 보통 처리하는데,
    // 지금은 찾을 수 없는 Http 상태코드 404를 뱉어야 하거든요. 저희는 api 형태로도 쓰이고 있으니 예외처리를 바꿔주셨으면 합니다.
    // JSA : 수정 완료했습니다.
    @Transactional
    @Override
    public void deletePost(Long boardId, String currentUsername, String userRole) {
        // 게시글 존재 확인
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("해당 게시글이 없습니다. id = " + boardId));

        // 권한 확인
        if (!canDelete(board, currentUsername, userRole)) {
            throw new UnauthorizedDeleteException("삭제 권한이 없습니다.");
        }

        // 실제 삭제 시도
        try {
            boardRepository.delete(board);
        } catch (Exception e) {
            throw new BoardDeletionException("삭제 처리 중 오류가 발생했습니다.");
        }
    }

    private boolean canDelete(Board board, String currentUsername, String userRole) {
        // 관리자는 모든 게시글 삭제 기능
        if ("ROLE_ADMIN".equals(userRole)) {
            return true;
        }
        // 게시글 작성자 본인만 삭제 가능
        return board.getUsername().equals(currentUsername);
    }

    // 게시글 목록 조회
    @Override
    public List<Board> getAllPosts() {
        return boardRepository.findAll();
    }

    // 게시글 상세 조회 (자기 게시글이거나 관리자일 경우 조회수 증가 없음 구현하자)
    @Transactional
    @Override
    public Board getPostByPostId(Long id) {
        Board post = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        post.increaseViewCount();
        return boardRepository.save(post);
    }

    // 게시글 수정 관련 함수 추가 2025.10.04 JHE
    @Override
    @Transactional(readOnly = true)
    public boolean isAuthor(Long boardId, Long userId) {
        // author가 null일 수 있으므로 authorId만 가볍게 조회
        Optional<Long> authorIdOpt = boardRepository.findAuthorIdByBoardId(boardId);
        if (authorIdOpt.isEmpty()) return false; // 글 없음
        Long authorId = authorIdOpt.get();
        if (authorId == null) return false;      // 탈퇴/분리된 글 → 누구도 수정 불가
        return authorId.equals(userId);
    }

    @Override
    @Transactional
    public void updatePost(Long boardId, Long actorUserId, UpdatePostDto updatePostDto) {
        Board board = getPostByPostId(boardId);

        // 1) 작성자 존재 검증 (author == null 이면 수정 금지)
        if (board.getAuthor() == null) {
            throw new SecurityException("작성자가 분리된 게시글은 수정할 수 없습니다.");
        }

        // 2) 권한 검증 (작성자 본인만)
        if (!board.getAuthor().getId().equals(actorUserId)) {
            throw new SecurityException("작성자 본인만 수정할 수 있습니다.");
        }

        board.updateBoard(updatePostDto.getTitle(), updatePostDto.getContent());

    }

}

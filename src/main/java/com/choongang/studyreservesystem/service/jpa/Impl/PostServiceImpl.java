package com.choongang.studyreservesystem.service.jpa.Impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.domain.BoardLike;
import com.choongang.studyreservesystem.domain.User;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.dto.SearchPostDto;
import com.choongang.studyreservesystem.dto.UpdatePostDto;
import com.choongang.studyreservesystem.exception.BoardDeletionException;
import com.choongang.studyreservesystem.exception.BoardNotFoundException;
import com.choongang.studyreservesystem.exception.UnauthorizedDeleteException;
import com.choongang.studyreservesystem.repository.jpa.BoardLikeRepository;
import com.choongang.studyreservesystem.repository.jpa.BoardRepository;
import com.choongang.studyreservesystem.repository.jpa.UserRepository;
import com.choongang.studyreservesystem.service.jpa.PostService;

import lombok.RequiredArgsConstructor;

// 최상위에서 Transactional 걸 수 있습니다.
// 근데 최상위에서 읽기만 하라고 걸면, 다른 수정해야 하는 create, delete 작업이 제한됩니다.
// 왜냐, 이 클래스의 모든 메서드는 읽기만 가능하다고 설정했기 때문에요.
// JSA : 최상위 Transactional은 클래스 레벨의 설정 오버라이드하기 때문에 하위 메서드들의 @Transactional은 모두 readOnly=false가 되는게 아닌가요?
// 수업시간에 배운 적이 있어 혹시 몰라 말씀드려봅니다
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

	private final BoardRepository boardRepository;
	private final BoardLikeRepository boardLikeRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public void createPost(CreatePostDto createPostDto) {
		Board board = Board.builder().title(createPostDto.getTitle()).content(createPostDto.getContent())
				.username(createPostDto.getUsername()).likeCount(0L).build();
		Board savedBoard = boardRepository.save(board);
	}

	// 게시글이 없다는 것은 BoardNotFoundException 이라는 네이밍을 가진 클래스를 사용하면 좋을 것 같아요.
	// 지금 쓰신 예외는 인수 입력 처리에 발생하는 에러입니다.
	// 저 예외처리를 받아서 핸들링 하는 다른 클래스와 혼동되는 문제가 생겨요 -> 예) 해당 예외처리하는 메서드는 400으로 보통 처리하는데,
	// 지금은 찾을 수 없는 Http 상태코드 404를 뱉어야 하거든요. 저희는 api 형태로도 쓰이고 있으니 예외처리를 바꿔주셨으면 합니다.
	// JSA : 수정 완료했습니다.
	@Transactional
	@Override
	public void deletePost(Long postId, String currentUsername, String userRole) {
		// 게시글 존재 확인
		Board board = boardRepository.findById(postId)
				.orElseThrow(() -> new BoardNotFoundException("해당 게시글이 없습니다. id = " + postId));

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
	public Page<Board> getAllPosts(Pageable pageable) {
		return boardRepository.findAll(pageable);
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
	public boolean isAuthor(Long postId, Long userId) {
		// author가 null일 수 있으므로 authorId만 가볍게 조회
		Optional<Long> authorIdOpt = boardRepository.findAuthorIdByPostId(postId);
		if (authorIdOpt.isEmpty()) {
			return false; // 글 없음
		}
		Long authorId = authorIdOpt.get();
		if (authorId == null) {
			return false; // 탈퇴/분리된 글 → 누구도 수정 불가
		}
		return authorId.equals(userId);
	}

	@Override
	@Transactional
	public void updatePost(Long postId, Long actorUserId, UpdatePostDto updatePostDto) {
		Board board = getPostByPostId(postId);

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

	@Override
	@Transactional
	public void toggleLike(Long postId, Long userId) {
		Optional<BoardLike> existingLike = boardLikeRepository.findByBoardPostIdAndUserId(postId, userId);
		Board board = boardRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

		if (existingLike.isPresent()) {
			// 좋아요 취소
			boardLikeRepository.findByBoardPostIdAndUserId(postId, userId);
		} else {
			// 좋아요 추가
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

			BoardLike boardLike = BoardLike.builder().board(board).user(user).build();
			boardLikeRepository.save(boardLike);
		}

		// 좋아요 수 업데이트
		long likeCount = boardLikeRepository.countByBoardPostId(postId);
		board.updateLikeCount(likeCount);
		boardRepository.save(board);
	}

	@Override
	public Page<Board> searchPosts(SearchPostDto searchPostDto, Pageable pageable) {
		String keyword = searchPostDto.getKeyword();
		String searchType = searchPostDto.getSearchType();

		if (keyword == null || keyword.trim().isEmpty()) {
			return boardRepository.findAll(pageable);
		}

		return switch (searchType) {
		case "title" -> boardRepository.findByTitleContaining(keyword, pageable);
		case "content" -> boardRepository.findByContentContaining(keyword, pageable);
		default -> boardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
		};
	}
}

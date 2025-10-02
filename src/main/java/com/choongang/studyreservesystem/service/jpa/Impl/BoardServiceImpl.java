package com.choongang.studyreservesystem.service.jpa.Impl;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.exception.BoardDeletionException;
import com.choongang.studyreservesystem.exception.BoardNotFoundException;
import com.choongang.studyreservesystem.exception.UnauthorizedDeleteException;
import com.choongang.studyreservesystem.repository.jpa.BoardRepository;
import com.choongang.studyreservesystem.service.jpa.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 최상위에서 Transactional 걸 수 있습니다.
// 근데 최상위에서 읽기만 하라고 걸면, 다른 수정해야 하는 create, delete 작업이 제한됩니다.
// 왜냐, 이 클래스의 모든 메서드는 읽기만 가능하다고 설정했기 때문에요.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    // 마지막으로 BoardRepository 흠.. 이것도 DIP 원칙에 위배되요.
    // BoardRepository는 인터페이스 아니야? 왜 DIP에 위배 돼? 라고 생각하실 수 있는데,
    // BoardRepository에 달린 JPA 인터페이스가 기억나시요?
    // JPA에 종속된 나름의 구현체라고 생각하시면 좋을 것 같습니다.
    // 기억이 나실진 모르겠지만, 전 MyBatis를 쓴다고 했어요. 그럼 전 Mapper라는 클래스를 사용해야 해요.
    // 지금의 상태로는 MyBatis도 적용하려면, 또 다른 인터페이스를 BoardService가 가져야 합니다.
    // 예시로, example이라는, 패키지에 제가 예시를 만들어드렸습니다. 참고해주시면 감사합니다.
    // 안고쳐도 됩니다. 이건 저희의 설계원칙에 대한 위배사항을 말씀드렸습니다.
    private final BoardRepository boardRepository;

    // 말씀하신 내용에 대해서 의견 감사합니다. 말씀하신 의견은 타당하다고 생각합니다.
    // 다만 제가 말씀은 드린 것은, 그러면 PostService를 만드셨어야 된다고 생각합니다.
    // 누구나 다 게시판(Board)에 생성하는 게시글(Post)을 만든다 라는 의견은 변함이 없으니까요.
    // 다만, 네이밍 컨벤션이 다르다는 것 자체에 주목하고 싶습니다. BoardService라는 것은 Board를 관리하는 주체에요.
    // (게시글 생성과 같은 CRUD가 아닌 게시판(Board) 자체에 대한 변경을 의미하는 서비스가 되어버립니다.)
    // Post를 만든 다는 것은 네이밍 자체에있어서 단일책임 원칙에 위반되는 사항이에요.
    // '나'만 개발하는 것이 아니라서 말씀드린 것입니다. 의견 감사합니다. 타당한 의견이기에 제가 생각하는? 것을 말씀드렸습니다.
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
}

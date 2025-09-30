package com.choongang.studyreservesystem.service.jpa;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.dto.CreatePostDto;
import com.choongang.studyreservesystem.repository.jpa.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// 추상 클래스에 의존해야 해요.
// DIP 원칙에 위배됩니다. BoardService라는 인터페이스를 구축해서 작성해주세요.
// 뭐 저희 개발에는 당장은 필요 없을지 몰라도, 나중에 직장가면 필요할 수 있으니까요.
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 엔티티 클래스 이름이 Board인데, createPost는 네이밍이 조금..? 헷갈리는 부분이 있는 것 같습니다.
    // 이 친구도 Transcational이 있으면 좋겠네요.
    public Long createPost(CreatePostDto createPostDto) {
        Board board = Board.builder()
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .username(createPostDto.getUsername())
                .createdAt(LocalDateTime.now())
                .likeCount(0L)
                .build();
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getPostId();
    }

    // 게시글이 없다는 것은 BoardNotFoundException 이라는 네이밍을 가진 클래스를 사용하면 좋을 것 같아요.
    // 지금 쓰신 예외는 인수 입력 처리에 발생하는 에러입니다.
    // 저 예외처리를 받아서 핸들링 하는 다른 클래스와 혼동되는 문제가 생겨요 -> 예) 해당 예외처리하는 메서드는 400으로 보통 처리하는데,
    // 지금은 찾을 수 없는 Http 상태코드 404를 뱉어야 하거든요. 저희는 api 형태로도 쓰이고 있으니 예외처리를 바꿔주셨으면 합니다.
    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        boardRepository.delete(board);
    }
}
package com.choongang.studyreservesystem.dto;

import com.choongang.studyreservesystem.domain.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdatePostDto {

    private Long id;

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String content;

    // 동시수정 방지용
    private Long version;

    // 필요 시 카테고리/태그/첨부 등 추가 필드들…

    public static UpdatePostDto from(Board board) {
        if (board == null) throw new IllegalArgumentException("board is null");
        return UpdatePostDto.builder()
                .id(board.getPostId())      // ← 엔티티의 PK 이름에 맞추세요 (예: getId() 라면 그걸 사용)
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}

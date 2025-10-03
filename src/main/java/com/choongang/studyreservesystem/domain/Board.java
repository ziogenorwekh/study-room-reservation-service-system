package com.choongang.studyreservesystem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Board extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardId;

	@Column( unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 1000)
	private String content;

	@Builder.Default
	private Long likeCount = 0L;

	@Builder.Default
	private Long viewCount = 0L;

	public void increaseViewCount() {
		this.viewCount++;
	}

    @Version
    private Long version;   // 동시 수정 방지

    // 작성자 null 허용(탈퇴 사용자)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "author_id", nullable = true) // nullable 허용
    private User author;

}
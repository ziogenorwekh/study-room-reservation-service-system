package com.choongang.studyreservesystem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "member")  // user 대신 member로 테이블 이름 지정(SYNTAX ERROR)
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	private String name;
	private String email;
	
	@Column(nullable = false)
	private String role;

    // 게시글은 그대로 두고 작성자만 NULL 처리(탈퇴 사용자)
    @OneToMany(mappedBy = "author", orphanRemoval = false)
    private List<Board> boards = new ArrayList<>();

}

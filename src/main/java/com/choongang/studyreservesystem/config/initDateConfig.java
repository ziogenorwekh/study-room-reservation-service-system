package com.choongang.studyreservesystem.config;

import com.choongang.studyreservesystem.domain.Board;
import com.choongang.studyreservesystem.domain.User;
import com.choongang.studyreservesystem.repository.jpa.BoardRepository;
import com.choongang.studyreservesystem.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class initDateConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // User 데이터베이스가 비어있는 경우에만 초기 데이터 생성
        List<User> users = userRepository.findAll();
        if (users.size() == 0) {

            // 첫 번째 유저 생성 및 저장
            User user1 = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .name("관리자")
                    .email("admin@example.com")
                    .role("ROLE_ADMIN")
                    .build();
            userRepository.save(user1);

            // 두 번째 유저 생성 및 저장
            User user2 = User.builder()
                    .username("user1")
                    .password(passwordEncoder.encode("user123"))
                    .name("사용자1")
                    .email("user1@example.com")
                    .role("ROLE_USER")
                    .build();
            userRepository.save(user2);

            // 세 번째 유저 생성 및 저장
            User user3 = User.builder()
                    .username("user2")
                    .password(passwordEncoder.encode("user123"))
                    .name("사용자2")
                    .email("user2@example.com")
                    .role("ROLE_USER")
                    .build();
            userRepository.save(user3);
        }

        // Board 데이터베이스가 비어있는 경우에만 초기 데이터 생성
        List<Board> boards = boardRepository.findAll();
        if (boards.size() == 0) {
            
            Board board1 = Board.builder()
                    .username("admin")
                    .title("스프링 부트 학습 후기")
                    .content("스프링 부트를 학습하면서 느낀 점들을 공유합니다. 자동 설정과 스타터의 편리함이 인상적이었습니다.")
                    .likeCount(15L)
                    .viewCount(120L)
                    .build();
            boardRepository.save(board1);
            
            Board board2 = Board.builder()
                    .username("user1")
                    .title("JPA 사용법 정리")
                    .content("JPA의 기본 개념부터 실제 프로젝트 적용까지 상세히 설명합니다. 엔티티 매핑과 연관관계 설정이 핵심입니다.")
                    .likeCount(23L)
                    .viewCount(89L)
                    .build();
            boardRepository.save(board2);
            
            Board board3 = Board.builder()
                    .username("user2")
                    .title("데이터베이스 설계 팁")
                    .content("효율적인 데이터베이스 설계를 위한 핵심 원칙들을 소개합니다. 정규화와 인덱스 설계가 중요합니다.")
                    .likeCount(8L)
                    .viewCount(156L)
                    .build();
            boardRepository.save(board3);
            
            Board board4 = Board.builder()
                    .username("admin")
                    .title("REST API 베스트 프랙티스")
                    .content("RESTful API 설계 원칙과 베스트 프랙티스를 정리했습니다. HTTP 메서드 사용법과 상태 코드 처리가 핵심입니다.")
                    .likeCount(31L)
                    .viewCount(203L)
                    .build();
            boardRepository.save(board4);
            
            Board board5 = Board.builder()
                    .username("user1")
                    .title("자바 성능 최적화")
                    .content("자바 애플리케이션의 성능을 향상시키는 다양한 기법들을 소개합니다. 메모리 관리와 GC 튜닝이 중요합니다.")
                    .likeCount(19L)
                    .viewCount(134L)
                    .build();
            boardRepository.save(board5);
            
            Board board6 = Board.builder()
                    .username("user2")
                    .title("리액트와 스프링 연동")
                    .content("리액트 프론트엔드와 스프링 백엔드를 연동하는 방법을 단계별로 설명합니다. CORS 설정과 API 통신이 핵심입니다.")
                    .likeCount(27L)
                    .viewCount(178L)
                    .build();
            boardRepository.save(board6);
            
            Board board7 = Board.builder()
                    .username("admin")
                    .title("마이크로서비스 아키텍처")
                    .content("마이크로서비스 아키텍처의 장단점과 구현 방법을 상세히 다룹니다. 서비스 간 통신과 데이터 일관성이 중요합니다.")
                    .likeCount(42L)
                    .viewCount(267L)
                    .build();
            boardRepository.save(board7);
            
            Board board8 = Board.builder()
                    .username("user1")
                    .title("Docker 컴테이너 활용법")
                    .content("Docker를 이용한 컴테이너화 방법과 실제 프로젝트 적용 사례를 소개합니다. Dockerfile 작성과 이미지 최적화가 핵심입니다.")
                    .likeCount(35L)
                    .viewCount(198L)
                    .build();
            boardRepository.save(board8);
            
            Board board9 = Board.builder()
                    .username("user2")
                    .title("AWS 클라우드 서비스")
                    .content("AWS의 주요 서비스들을 소개하고 실제 프로젝트에 적용하는 방법을 설명합니다. EC2, RDS, S3 사용법이 핵심입니다.")
                    .likeCount(29L)
                    .viewCount(145L)
                    .build();
            boardRepository.save(board9);
            
            Board board10 = Board.builder()
                    .username("admin")
                    .title("코드 리뷰 가이드라인")
                    .content("효과적인 코드 리뷰를 위한 가이드라인과 베스트 프랙티스를 소개합니다. 코드 품질 향상과 팀 협업에 도움이 됩니다.")
                    .likeCount(18L)
                    .viewCount(167L)
                    .build();
            boardRepository.save(board10);
            
            Board board11 = Board.builder()
                    .username("user1")
                    .title("Git 브랜치 전략")
                    .content("Git Flow, GitHub Flow 등 다양한 브랜치 전략을 비교하고 프로젝트에 맞는 전략을 선택하는 방법을 설명합니다.")
                    .likeCount(22L)
                    .viewCount(112L)
                    .build();
            boardRepository.save(board11);
            
            Board board12 = Board.builder()
                    .username("user2")
                    .title("테스트 주도 개발 TDD")
                    .content("TDD의 기본 개념과 실제 적용 방법을 단계별로 설명합니다. 단위 테스트 작성과 리팩토링이 핵심입니다.")
                    .likeCount(33L)
                    .viewCount(189L)
                    .build();
            boardRepository.save(board12);
            
            Board board13 = Board.builder()
                    .username("admin")
                    .title("디자인 패턴 학습")
                    .content("자주 사용되는 디자인 패턴들을 실제 코드 예시와 함께 설명합니다. 싱글톤, 팩토리, 옵저버 패턴 등을 다룹니다.")
                    .likeCount(26L)
                    .viewCount(143L)
                    .build();
            boardRepository.save(board13);
            
            Board board14 = Board.builder()
                    .username("user1")
                    .title("알고리즘 문제 해결")
                    .content("코딩 테스트와 알고리즘 문제 해결을 위한 전략과 팁을 공유합니다. 시간 복잡도와 공간 복잡도 분석이 중요합니다.")
                    .likeCount(41L)
                    .viewCount(234L)
                    .build();
            boardRepository.save(board14);
            
            Board board15 = Board.builder()
                    .username("user2")
                    .title("데이터 구조와 알고리즘")
                    .content("기본적인 데이터 구조들의 특징과 사용 예시를 소개합니다. 배열, 링크드 리스트, 스택, 큐 등을 다룹니다.")
                    .likeCount(17L)
                    .viewCount(98L)
                    .build();
            boardRepository.save(board15);
            
            Board board16 = Board.builder()
                    .username("admin")
                    .title("웹 보안 기초")
                    .content("웹 애플리케이션 보안의 기본 개념과 주요 위협 요소들을 소개합니다. XSS, CSRF, SQL 인젝션 방어법을 다룹니다.")
                    .likeCount(38L)
                    .viewCount(201L)
                    .build();
            boardRepository.save(board16);
            
            Board board17 = Board.builder()
                    .username("user1")
                    .title("HTTPS 설정 방법")
                    .content("SSL/TLS 인증서 설치와 HTTPS 설정 방법을 단계별로 설명합니다. Let's Encrypt를 이용한 무료 인증서 발급법도 다룹니다.")
                    .likeCount(24L)
                    .viewCount(156L)
                    .build();
            boardRepository.save(board17);
            
            Board board18 = Board.builder()
                    .username("user2")
                    .title("로그 분석 도구")
                    .content("애플리케이션 로그 분석을 위한 다양한 도구들을 비교하고 사용법을 소개합니다. ELK 스택과 Grafana 활용법을 다룹니다.")
                    .likeCount(31L)
                    .viewCount(187L)
                    .build();
            boardRepository.save(board18);
            
            Board board19 = Board.builder()
                    .username("admin")
                    .title("모니터링 시스템 구축")
                    .content("시스템 모니터링과 알림 시스템 구축 방법을 상세히 설명합니다. Prometheus와 Grafana를 이용한 모니터링 구축 사례를 다룹니다.")
                    .likeCount(45L)
                    .viewCount(278L)
                    .build();
            boardRepository.save(board19);
            
            Board board20 = Board.builder()
                    .username("user1")
                    .title("애자일 방법론 소개")
                    .content("애자일 개발 방법론의 기본 원칙과 실제 프로젝트 적용 방법을 소개합니다. 스크럼과 칸반 방법론을 비교 설명합니다.")
                    .likeCount(28L)
                    .viewCount(165L)
                    .build();
            boardRepository.save(board20);
            
            Board board21 = Board.builder()
                    .username("user2")
                    .title("개발자 커리어 로드맵")
                    .content("개발자로서의 커리어 경로와 각 단계별 필요한 역량들을 정리했습니다. 주니어부터 시니어까지의 성장 경로를 제시합니다.")
                    .likeCount(52L)
                    .viewCount(312L)
                    .build();
            boardRepository.save(board21);
        }
    }
}

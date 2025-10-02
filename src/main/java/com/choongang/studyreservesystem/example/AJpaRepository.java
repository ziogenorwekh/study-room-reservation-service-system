package com.choongang.studyreservesystem.example;

import com.choongang.studyreservesystem.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public class AJpaRepository implements ARepository {

    private final JpaRepository<Board,Long> repo;

    public AJpaRepository(JpaRepository<Board, Long> repo) {
        this.repo = repo;
    }

    @Override
    public void save() {

    }
}

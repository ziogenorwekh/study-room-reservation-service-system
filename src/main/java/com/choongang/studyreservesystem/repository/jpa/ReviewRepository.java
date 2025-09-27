package com.choongang.studyreservesystem.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choongang.studyreservesystem.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}

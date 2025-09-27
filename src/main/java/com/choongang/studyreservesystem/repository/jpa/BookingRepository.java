package com.choongang.studyreservesystem.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choongang.studyreservesystem.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}

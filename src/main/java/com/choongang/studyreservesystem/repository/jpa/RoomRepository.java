package com.choongang.studyreservesystem.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choongang.studyreservesystem.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}

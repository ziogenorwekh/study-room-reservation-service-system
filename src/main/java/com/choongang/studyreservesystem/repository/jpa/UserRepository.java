package com.choongang.studyreservesystem.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choongang.studyreservesystem.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByUsername(String username);
	boolean existsByUsernameAndEmail(String username, String email);

	Optional<User> findByUsername(String username);
	List<User> findBynameAndEmail(String name, String email);
	Optional<User> findByUsernameAndEmail(String username, String email);
}

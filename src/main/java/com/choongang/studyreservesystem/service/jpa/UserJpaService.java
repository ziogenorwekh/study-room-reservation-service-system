package com.choongang.studyreservesystem.service.jpa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.choongang.studyreservesystem.domain.User;
import com.choongang.studyreservesystem.dto.UserFindUsernameDto;
import com.choongang.studyreservesystem.dto.UserPasswordChangeDto;
import com.choongang.studyreservesystem.dto.UserRegisterDto;
import com.choongang.studyreservesystem.dto.UserResponseDto;
import com.choongang.studyreservesystem.repository.jpa.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserJpaService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;

	public List<UserResponseDto> findAll() {
		List<User> allList = userRepository.findAll();
		List<UserResponseDto> dtos = new ArrayList<>();
		for (User user : allList) {
			dtos.add(new UserResponseDto(user.getName(), user.getEmail()));
		}
		return dtos;
	}

	public UserResponseDto findById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException(id + "의 User를 찾을 수 없습니다."));
		UserResponseDto dto = new UserResponseDto();
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		return dto;
	}

	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public void register(UserRegisterDto dto) {
		User user = new User();
		if (userRepository.existsByUsername(dto.getUsername())) {
			throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
		}
		user = User.builder().username(dto.getUsername()).password(encoder.encode(dto.getPassword())).role("ROLE_USER")
				.name(dto.getName()).email(dto.getEmail()).build();
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public List<UserFindUsernameDto> findUserNameFromNameAndEmail(String name, String email) {
		List<User> users = userRepository.findBynameAndEmail(name, email);
		if (users == null) {
			return null;
		}
		List<UserFindUsernameDto> dtos = new ArrayList<>();
		for (User user : users) {
			dtos.add(new UserFindUsernameDto(user.getUsername()));
		}
		return dtos;
	}

	@Transactional(readOnly = true)
	public boolean matchUsernameAndEmail(UserPasswordChangeDto dto) {
		return userRepository.existsByUsernameAndEmail(dto.getUsername(), dto.getEmail());
	}

	public void passwordChange(UserPasswordChangeDto dto) {
		User user = userRepository.findByUsername(dto.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
		User updatedUser = User.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail())
				.role(user.getRole()).password(encoder.encode(dto.getChangePassword())).build();
		userRepository.save(updatedUser);
	}
}

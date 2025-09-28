package com.choongang.studyreservesystem.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.choongang.studyreservesystem.dto.UserFindUsernameDto;
import com.choongang.studyreservesystem.service.jpa.UserJpaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainRestControllers {

	private final UserJpaService userService;

	@GetMapping("/check-username")
	public Map<String, Boolean> checkUsername(@RequestParam String username) {
		Map<String, Boolean> check = new HashMap<>();
		check.put("available", !userService.existsByUsername(username));
		return check;
	}
	
	@PostMapping("/find")
	public Map<String,List<String>> findByNameAndEmail(@RequestBody  Map<String, String> payload) {
		String name = payload.get("name");
	    String email = payload.get("email");
		List<UserFindUsernameDto> dtos = userService.findUserNameFromNameAndEmail(name, email);
		List<String> usernames = new ArrayList<>();
		if (dtos == null || dtos.isEmpty()) {
			return Map.of("username",List.of());
		}
		for (UserFindUsernameDto userFindUsernameDto : dtos) {
			usernames.add(userFindUsernameDto.getUsername());
		}
		Map<String, List<String>> map = new HashMap<>();
		map.put("username", usernames);
		return map;
	}
}

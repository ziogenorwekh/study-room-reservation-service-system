package com.choongang.studyreservesystem.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.choongang.studyreservesystem.dto.UserPasswordChangeDto;
import com.choongang.studyreservesystem.dto.UserRegisterDto;
import com.choongang.studyreservesystem.exception.UserDuplicationException;
import com.choongang.studyreservesystem.service.jpa.UserJpaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final UserJpaService userJpaService;

	@GetMapping("/")
	public String home(Model model) {
		String id = SecurityContextHolder.getContext().getAuthentication().getName();
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next()
				.getAuthority();
		model.addAttribute("id", id);
		model.addAttribute("role", role);
		return "home";
	}

	@GetMapping("/register")
	public String userRegister(Model model) {
		UserRegisterDto dto = new UserRegisterDto();
		model.addAttribute("user", dto);
		return "user_join/register";
	}

	@PostMapping("/register")
	public String postMethodName(UserRegisterDto dto, Model model) throws UserDuplicationException {
		userJpaService.register(dto);
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String userLogin() {
		return "user_join/login";
	}

	@GetMapping("/help")
	public String findIdAndPassword(Model model) {
		UserPasswordChangeDto dto = new UserPasswordChangeDto();
		model.addAttribute("user", dto);
		return "help";
	}

	@PostMapping("/reset-password")
	public String resetPassword(UserPasswordChangeDto dto, Model model) {
		if (userJpaService.matchUsernameAndEmail(dto)) {
			model.addAttribute("user", dto);
			return "user/password-change";
		}
		return "redirect:/help";
	}

	@PostMapping("/change-password")
	public String methodName(UserPasswordChangeDto dto) {
		userJpaService.passwordChange(dto);
		return "redirect:/login";
	}
}

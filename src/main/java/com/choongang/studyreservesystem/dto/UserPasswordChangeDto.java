package com.choongang.studyreservesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordChangeDto {
	private String username;
	private String changePassword;
	private String email;

}

package com.choongang.studyreservesystem.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.choongang.studyreservesystem.domain.User;

@SuppressWarnings("serial")
public class UserLoginDto implements UserDetails {
	private User user;

	public UserLoginDto(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collection;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

//	@Override
//	public boolean isAccountNonExpired() {
//		return !user.isDeleted();
//	}
//
//
//	@Override
//	public boolean isEnabled() {
//		return !user.isDeleted();
//	}

}

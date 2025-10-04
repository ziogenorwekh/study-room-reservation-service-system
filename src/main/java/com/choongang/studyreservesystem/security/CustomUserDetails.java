package com.choongang.studyreservesystem.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.choongang.studyreservesystem.domain.User;
public class CustomUserDetails implements UserDetails {

    private final User user;  // JPA 엔티티 보유

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /** 우리 서비스에서 자주 쓰는 PK 꺼내기 */
    public Long getId() {
        return user.getId();
    }

    /** 필요하면 도메인 엔티티 자체 접근 */
    public User getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /** role(String)을 권한으로 변환 (ROLE_ 접두어 처리) */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();
        String normalized = role != null && role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return List.of(new SimpleGrantedAuthority(normalized));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked()  { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /** BaseEntity.deleted 활용: deleted=false 일 때만 활성 */
    @Override
    public boolean isEnabled() { return !user.isDeleted(); }

}

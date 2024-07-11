package com.sparta.kanbanboard.common.security;


import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Member member;

    public UserDetailsImpl(Member member) {
        this.member = member;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        MemberRole role = member.getRole();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getAuthority());

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }
}
package com.sparta.kanbanboard.domain.member.entity;

import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.ALREADY_LOGOUT;

import com.sparta.kanbanboard.common.base.entity.Timestamped;
import com.sparta.kanbanboard.common.exception.customexception.MemberAlreadyLogoutException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Column
    private String refreshToken;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(String email, String password, String name, MemberRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * Member 생성
     */
    public static Member createMember(String email, String password, String name, MemberRole role) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(role)
                .build();
    }

    /**
     * refresh 토큰 저장
     */
    public void saveRefreshToken(String token) {
        this.refreshToken = token;
    }

    /**
     * refresh 토근 삭제
     */
    public void deleteToken() {
        if(!StringUtils.hasText(this.refreshToken)){
            throw new MemberAlreadyLogoutException(ALREADY_LOGOUT);
        }
        this.refreshToken = "";
    }

    /**
     * 비밀번호 변경
     */
    public void updatePwd(String password) {
        this.password = password;
    }

    /**
     * 프로필 수정
     */
    public void updateProfile(String name) {
        this.name = name;
    }
}

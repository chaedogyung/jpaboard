package com.jpaboard.config.oauth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 session을 만들어 줍니다.(Security ContextHolder)
import com.jpaboard.entity.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;//콤포지션
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //OAuth 로그인
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    //해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                String role = "ROLE_" + member.getRole();
//                String role= String.valueOf(member.getRole());
                return role;
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getUserpass();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 우리 사이트! 1년 동안 회원이 로그인을 안 하면 휴먼 계정으로 처리
        // 현재 시간 - 로그인 시간 => 1년을 초과하면 return false;
        // 조건: loginDate 라는 컬럼을 추가해야 함
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}

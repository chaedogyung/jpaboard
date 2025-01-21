package com.jpaboard.config.oauth;


//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인 진행이 완료가 되면 session을 만들어 줍니다.(Security ContextHolder)

import com.jpaboard.entity.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {

    private Member member;//콤포지션

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(member.getRole());
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
        return getUsername();
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
        //우리 사이트! 1년동안 회원이 로그인을 안하면!! 휴먼 계정으로 하기로 함
        //현재 시간 - 로긴시간=> 1년을 초과하면 return false;
        //조건:longinDate 라는 컬럼을 만들어야 함
        return true;
    }
}

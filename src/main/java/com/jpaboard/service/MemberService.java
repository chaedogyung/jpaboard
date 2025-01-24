package com.jpaboard.service;

import com.jpaboard.config.oauth.PrincipalDetails;
import com.jpaboard.entity.Member;
import com.jpaboard.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member;
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByUseremail(member.getUseremail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        Member findId = memberRepository.findByUserid(member.getUserid());
        if (findId != null) {
            throw new IllegalStateException("동일한 아이디가 있습니다.");
        }
    }

    //시큐리티 session(내부 Authentication (내부 UserDetails))
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    //시큐리티 설정에서 loginProcessingUrl("/login")
    //login 요청이 오면 자동으로 UserDetailsService 타입으로 loC되어 있는 loadUserByUsername 함수가 실행
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUseremail(username);

        if (member != null) {
            System.out.println(member.getRole());
            return new PrincipalDetails(member);

        } else {
            return null;
        }
    }
}

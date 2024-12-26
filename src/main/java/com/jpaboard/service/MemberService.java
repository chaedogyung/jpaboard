package com.jpaboard.service;

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

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        Member member = memberRepository.findByUseremail(useremail);

        if (member == null) {
            throw new UsernameNotFoundException(useremail);
        }

        return User.builder()
                .username(member.getUsername())
                .password(member.getUserpass())
                .roles(member.getRole().toString())
                .build();

    }
}

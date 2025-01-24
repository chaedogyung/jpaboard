//package com.jpaboard.config.oauth;
//
//import com.jpaboard.entity.Member;
//import com.jpaboard.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
////시크리티 설정에서 loginProcessingUrl("/login")
//// /login 요청이 오면 자동으로 UserDetailsService 타입으로 loC되어 있는 loadUserByUsername 함수가 실행
//@Service
//public class PrincipalDetailsSerivice implements UserDetailsService{
//
//    @Autowired
//    private MemberRepository memberRepository;
//
////    //시큐리티 session(내부 Authentication (내부 UserDetails))
////    함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
////    @Override
////    public UserDetails loadUserByUsername(String usermail) throws UsernameNotFoundException {
////        Member memberEntity = memberRepository.findByUseremail(usermail);
////        if (memberEntity == null) {
////            return new PrincipalDetails(memberEntity);
////        }
////        return null;
////    }
//}

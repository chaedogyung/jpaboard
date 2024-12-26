package com.jpaboard.controller;

import com.jpaboard.dto.MemberFormDto;
import com.jpaboard.entity.Member;
import com.jpaboard.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String useremail, String userpass, String userid) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setUseremail(useremail);
        memberFormDto.setUserid(userid);
        memberFormDto.setUsername("홍길동");
        memberFormDto.setUseraddress("서울시 마포구 합정동");
        memberFormDto.setUserpass(userpass);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String useremail = "test@email.com";
        String userpass = "1234";
        String userid = "aaa111";
        this.createMember(useremail, userpass, userid);
        mockMvc.perform(formLogin().userParameter("useremail")
                        .loginProcessingUrl("/members/login")
                        .user(useremail).password(userpass))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

//    @Test
//    @DisplayName("로그인 실패 테스트")
//    public void loginFailTest() throws Exception {
//        String useremail = "test@email.com";
//        String userpass = "1234";
//        this.createMember(useremail, userpass);
//        mockMvc.perform(formLogin().userParameter("useremail")
//                        .loginProcessingUrl("/members/login")
//                        .user(useremail).password("12345"))
//                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
//    }

}
package com.jpaboard.controller;

//import com.jpaboard.config.oauth.PrincipalDetails;
import com.jpaboard.constant.Role;
import com.jpaboard.dto.MemberFormDto;
import com.jpaboard.entity.Member;
import com.jpaboard.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    @GetMapping(value = "/test/login")
//    public String testLogin(Authentication authentication
//            , @AuthenticationPrincipal PrincipalDetails userDetails) {
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        System.out.println("authentication :" + principalDetails.getMember());
//        System.out.println("userDetails :" + userDetails.getMember());
//        return "세션정보 확인하기";
//    }
//
//    @GetMapping(value = "/test/oauth/login")
//    public String testOAuthLogin(
//            Authentication authentication) {
//
//        OAuth2User auth2User = (OAuth2User) authentication.getPrincipal();
//        System.out.println("authentication :" + auth2User.getAttributes());
//
//        return "Oauth 세션정보 확인하기";
//    }

    @GetMapping(value = "/newReg")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid Member member,
                            BindingResult bindingResult,
                            Model model) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {

            String rawPassword = member.getUserpass();
            String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
            member.setRole(Role.ADMIN);
            member.setUserpass(encodedPassword);

            memberService.saveMember(member);
            return "redirect:/member/memberForm";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }
}

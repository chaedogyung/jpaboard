package com.jpaboard.config;

import com.jpaboard.config.oauth.PrincipalDetails;
import com.jpaboard.constant.Role;
import com.jpaboard.entity.Member;
import com.jpaboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("userRequest:" + userRequest.getClientRegistration());//registration로 어떤 OAuth로 로그인 했는지 확인가능
        System.out.println("getAccessToken :" + userRequest.getAccessToken().getTokenValue());

        OAuth2User oauth2User = super.loadUser(userRequest);
        //구글 로그인 버튼 클릭-> 구글로그인창->로그인 완료-> code를 리턴(OAuth-Client라이브러리)->AccessToken요청
        //userRequest 정보 -> loadUser함수 호출 ->구글로부터 회원프로필 받아준다
        System.out.println("getAttributes :" + oauth2User.getAttributes());

        //회원가입을 강제로 진행해볼 예정
        String provider = userRequest.getClientRegistration().getRegistrationId();//google
        String providerId = oauth2User.getAttribute("sub");
        String username = oauth2User.getAttribute("name");
        String userid = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String useremail = oauth2User.getAttribute("email");
        String role = Role.USER.name();

        Member memberEntity = memberRepository.findByUsername(username);

        if (memberEntity == null) {
            System.out.println("구글 로그인이 최초 입니다.");

            memberEntity = Member.builder()
                    .userid(userid)
                    .username(username)
                    .useremail(useremail)
                    .userpass(password)
                    .role(Role.valueOf(role))
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(memberEntity);
        } else {
            System.out.println("구글 로그인을 이미 한적이 있습니다. 당신은 자동회원 가입되어 있습니다.");
        }

        return new PrincipalDetails(memberEntity, oauth2User.getAttributes());
    }
}

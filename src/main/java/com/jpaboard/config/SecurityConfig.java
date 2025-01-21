package com.jpaboard.config;

import com.jpaboard.config.oauth.PrincipalOauth2Service;
import com.jpaboard.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    PrincipalOauth2Service principalOauth2Service;

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // 공용 리소스
                        .requestMatchers("/", "/members/**", "/video/**", "/images/**").permitAll() // 공용 페이지
                        .requestMatchers("/user/video/manage", "/user/video/newReg","/user/video/new").hasRole("ADMIN") // ADMIN만 접근 가능
                        .requestMatchers("/secret/video/manage*", "/secret/video/newReg","/secret/video/new").hasRole("SECRET") // SECRET 접근 가능
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER와 ADMIN 모두 접근 가능
                        .anyRequest()
                        .authenticated() // 나머지 경로는 인증 필요
                )
                .formLogin(formLoginCustomizer -> formLoginCustomizer
                        .loginPage("/members/login") // 사용자 지정 로그인 페이지
                        .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트
                        .usernameParameter("useremail") // 사용자 이메일 파라미터
                        .failureHandler(new CustomAuthenticationFailureHandler()) // 로그인 실패 핸들러
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/members/login") // 사용자 지정 로그인 페이지
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2Service))// 사용자 정의 OAuth2 서비스 연결
                )
                .logout(logoutCustomizer -> logoutCustomizer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 사용자 지정 로그아웃
                        .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
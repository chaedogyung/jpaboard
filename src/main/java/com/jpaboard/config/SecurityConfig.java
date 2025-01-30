package com.jpaboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // 공용 리소스
                        .requestMatchers("/", "/members/**", "/video/**", "/images/**").permitAll() // 공용 페이지
                        .requestMatchers("/user/video/manage", "/user/video/newReg", "/user/video/new").hasRole("ADMIN") // ADMIN만 접근 가능
                        .requestMatchers("/secret/video/manage*", "/secret/video/newReg", "/secret/video/new").hasRole("SECRET") // SECRET 접근 가능
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER와 ADMIN 모두 접근 가능
                        .anyRequest()
                        .authenticated() // 나머지 경로는 인증 필요
                )
                .formLogin(formLoginCustomizer -> formLoginCustomizer
                        .loginPage("/members/login") // 사용자 지정 로그인 페이지
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트
                        .usernameParameter("username") // 사용자 이메일 파라미터
                        .failureHandler(new CustomAuthenticationFailureHandler()) // 로그인 실패 핸들러
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/members/login") // 사용자 지정 로그인 페이지
                        //구글 로그인인 완료된 뒤의 후처리가 필요함. tip,코드X,(엑세스 토큰+사용자프로필 정보0)
                        .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트
                        .failureHandler(new CustomAuthenticationFailureHandler()) // 로그인 실패 핸들러
                )
                .logout(logoutCustomizer -> logoutCustomizer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 사용자 지정 로그아웃
                        .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
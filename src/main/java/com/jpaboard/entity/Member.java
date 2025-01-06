package com.jpaboard.entity;

import com.jpaboard.constant.Role;
import com.jpaboard.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "mp_member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "USERID", nullable = false, unique = true) // 중복 방지 및 필수 값 설정
    private String userid; // 사용자가 입력한 ID

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "USEREMAIL", unique = true, nullable = false)
    private String useremail;

    @Column(name = "USERPASS", nullable = false)
    private String userpass;

    @Column(name = "USERADDRESS")
    private String useraddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    @Column(name = "REGDATE", nullable = false)
    private LocalDateTime regdate;

    // 엔티티가 저장되기 전 자동으로 현재 시간을 설정
    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now();
    }

    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setUserid(memberFormDto.getUserid()); // 사용자가 입력한 ID 설정
        member.setUserpass(passwordEncoder.encode(memberFormDto.getUserpass()));
        member.setUsername(memberFormDto.getUsername());
        member.setUseremail(memberFormDto.getUseremail());
        member.setUseraddress(memberFormDto.getUseraddress());
        member.setRole(Role.ADMIN);
        return member;
    }
}
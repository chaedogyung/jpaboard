package com.jpaboard.entity;

import com.jpaboard.constant.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mp_member")
@Data
@NoArgsConstructor
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

    @Column(name = "PROVIDER")
    private String provider;

    @Column(name = "PROVIDERID")
    private String providerId;

    @Column(name = "REGDATE", nullable = false)
    private LocalDateTime regdate;

    // 엔티티가 저장되기 전 자동으로 현재 시간을 설정
    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now();
    }

    @Builder
    public Member(String userid, String username, String useremail,
                  String userpass, String useraddress, Role role,
                  String provider, String providerId, LocalDateTime regdate) {
        this.userid = userid;
        this.username = username;
        this.useremail = useremail;
        this.userpass = userpass;
        this.useraddress = useraddress;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.regdate = regdate;
    }
}
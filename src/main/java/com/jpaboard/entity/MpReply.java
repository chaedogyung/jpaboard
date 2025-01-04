package com.jpaboard.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "MP_REPLY", schema = "STUDY")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MpReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 복합키 중 RE_RNO 자동 증가
    @Column(name = "RE_RNO", nullable = false)
    private Long reRno;

    @Column(name = "RE_CONTENT", nullable = false, length = 1000)
    private String reContent;

    @Column(name = "RE_WRITER", nullable = false, length = 50)
    private String reWriter;

    @Column(name = "RE_REGDATE", updatable = false)
    private LocalDateTime reRegDate = LocalDateTime.now(); // sysdate 기본값

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BNO", nullable = false)
    @ToString.Exclude
    private BoardVO boardVO; // 다대일 관계 매핑
}

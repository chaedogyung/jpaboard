package com.jpaboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MP_REPLY", schema = "STUDY")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@SequenceGenerator(
        name = "mp_reply_seq_generator",
        sequenceName = "mp_reply_seq",
        allocationSize = 1
)
public class MpReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "mp_reply_seq_generator") // 복합키 중 RE_RNO 자동 증가
    @Column(name = "RE_RNO", nullable = false)
    private Long reRno;

    @Column(name = "RE_CONTENT", nullable = false, length = 1000)
    private String reContent;

    @Column(name = "RE_WRITER", nullable = false, length = 50)
    private String reWriter;

    @Column(name = "RE_REGDATE", updatable = false)
    private LocalDateTime reRegDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BNO", nullable = false)
    @ToString.Exclude
    @JsonIgnore // 순환 참조 방지
    private BoardVO boardVO;

    // 엔티티 생성 시 기본값 설정
    @PrePersist
    public void prePersist() {
        this.reRegDate = this.reRegDate == null ? LocalDateTime.now() : this.reRegDate;
    }
}
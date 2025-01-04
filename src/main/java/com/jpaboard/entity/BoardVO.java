package com.jpaboard.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mp_board")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "mp_board_seq_generator", // Hibernate가 사용하는 시퀀스 제너레이터 이름
        sequenceName = "mp_board_seq",  // 실제 데이터베이스 시퀀스 이름
        allocationSize = 1             // Hibernate와 DB 시퀀스의 증가 값 일치
)
public class BoardVO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mp_board_seq_generator")
    @Column(name = "BNO", nullable = false)
    private Long bno;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "CONTENT", nullable = false, length = 2000)
    private String content;

    @Column(name = "WRITER", nullable = false, length = 100)
    private String writer;

    @Column(name = "REGDATE", updatable = false)
    private LocalDateTime regDate = LocalDateTime.now();

    @Column(name = "HIT", nullable = false)
    private Long hit = 0L;

    @OneToMany(mappedBy = "boardVO", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MpReply> replies = new ArrayList<>();
}
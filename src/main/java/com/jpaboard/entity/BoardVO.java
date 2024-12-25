package com.jpaboard.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "mp_board")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardVO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mp_board_seq")
    @SequenceGenerator(name = "mp_board_seq", sequenceName = "mp_board_seq", allocationSize = 1)
    private Long bno;

    private String title;

    private String content;

    private String writer;

    @Column(insertable = false, updatable = false) // DB에서 기본값 설정이 되어 있다면 사용
    private LocalDateTime regdate;

    private int hit;

}
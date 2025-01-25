package com.jpaboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MP_BOARD", schema = "STUDY") // 스키마 통일
@SequenceGenerator(
        name = "mp_board_seq_generator",
        sequenceName = "mp_board_seq",
        allocationSize = 1
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
    private LocalDateTime regDate;

    @Column(name = "UPDATEREGDATE", updatable = false)
    private LocalDateTime updateRegDate;

    @Column(name = "HIT", nullable = false)
    private Long hit = 0L;

    @OneToMany(mappedBy = "boardVO", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MpReply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "boardVO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BoardFile> files = new ArrayList<>(); // 첨부파일 목록

    // 엔티티 생성 시 기본값 설정
    @PrePersist
    public void prePersist() {
        this.regDate = this.regDate == null ? LocalDateTime.now() : this.regDate;
    }
}
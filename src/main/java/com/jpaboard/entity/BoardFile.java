package com.jpaboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "MP_FILE", schema = "STUDY")
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_NO")
    private Long fileNo; // 파일 번호

    @Column(name = "BNO", nullable = false)
    private Long bno; // 게시판 번호

    @Column(name = "ORG_FILE_NAME", nullable = false, length = 260)
    private String orgFileName; // 원본 파일 이름

    @Column(name = "STORED_FILE_NAME", nullable = false, length = 36)
    private String storedFileName; // 변경된 파일 이름

    @Column(name = "FILE_SIZE")
    private Long fileSize; // 파일 크기

    @Column(name = "REGDATE", nullable = false)
    private LocalDateTime regDate = LocalDateTime.now(); // 파일 등록일

    @Column(name = "DEL_GB", nullable = false, length = 1)
    private String delGb = "N"; // 삭제 구분

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BNO", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_MP_FILE_BNO"))
    @JsonIgnore // 순환 참조 방지
    private BoardVO boardVO; // 게시판과의 연관 관계
}

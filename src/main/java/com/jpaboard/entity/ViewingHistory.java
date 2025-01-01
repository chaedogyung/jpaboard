package com.jpaboard.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "VIEWING_HISTORY")
@Data
public class ViewingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long history_id; // 시청 기록 ID (고유 식별자)

    @OneToOne
    @JoinColumn(name = "USERID", nullable = false)
    private Member userid; // 외래 키, mp_member 테이블 참조

    @ManyToOne
    @JoinColumn(name = "VIDEO_ID", nullable = false)
    private Videos video_id; // 외래 키, videos 테이블 참조

    @Column(name = "VIEWED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp viewed_at; // 시청 시간

    @Column(name = "DURATION_WATCHED")
    private Long duration_watched; // 시청한 시간 (분 단위)

    @Column(name = "IS_COMPLETED", columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String is_completed; // 시청 완료 여부 (Y/N) // No need for explicit constructors, getters, and setters }

}

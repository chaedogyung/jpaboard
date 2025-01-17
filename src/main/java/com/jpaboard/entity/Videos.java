package com.jpaboard.entity;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "VIDEOS", schema = "STUDY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public
class Videos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VIDEO_ID")
    private Long videoId; // 비디오 ID (자동 증가, 고유 키)

    @Column(name = "TITLE", nullable = false)
    private String title; // 비디오 제목

    @Column(name = "DESCRIPTION", columnDefinition = "CLOB")
    private String description; // 비디오 설명 (긴 텍스트)

    @Column(name = "GENRE")
    private String genre; // 장르 (예: 액션, 드라마 등)

    @Column(name = "RELEASE_DATE")
    private Date release_date; // 출시일

    @Column(name = "DURATION")
    private Integer duration; // 영상 길이 (분 단위)

    @Column(name = "LANGUAGE")
    private String language; // 언어 (예: 한국어, 영어)

    @Column(name = "AGE_RATING")
    private String age_rating; // 연령 등급 (예: PG-13, R 등)

    @Column(name = "THUMBNAIL_URL")
    private String thumbnail_url; // 썸네일 이미지 URL

    @Column(name = "VIDEO_URL", nullable = false)
    private String video_url; // 비디오 URL

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created_at; // 등록일

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updated_at; // 수정일

    @Column(name = "SUBTITLE_FILE_PATH")
    private String subtitle_file_path; // 자막 파일 경로

    // VideoLikes와의 연관 관계 매핑 (OneToMany)
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoLikes> likes; // 좋아요 정보 리스트
}
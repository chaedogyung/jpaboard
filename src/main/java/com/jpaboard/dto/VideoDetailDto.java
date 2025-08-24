package com.jpaboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetailDto {
    private Long videoId;
    private String title;
    private String video_url;
    private String subtitle_file_path;
    private Date release_date;   // 추가
    private String age_rating;   // 추가
    private String genre;       // 추가
    private String language;    // 추가
    private String description; // 추가
    private Integer viewCount;
}

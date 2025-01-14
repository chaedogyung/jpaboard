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
    private String videoUrl;
    private String subtitleFilePath;
    private Date releaseDate;   // 추가
    private String ageRating;   // 추가
    private String genre;       // 추가
    private String language;    // 추가
    private String description; // 추가
    private Long likeCount;
}

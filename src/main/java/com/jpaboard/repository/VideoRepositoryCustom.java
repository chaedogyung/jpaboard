package com.jpaboard.repository;

import com.jpaboard.dto.VideoDetailDto;
import com.jpaboard.entity.Videos;

public interface VideoRepositoryCustom {
    VideoDetailDto findVideoDetailWithLikeCount(Long videoId);
}

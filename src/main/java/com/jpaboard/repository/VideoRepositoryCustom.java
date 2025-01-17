package com.jpaboard.repository;

import com.jpaboard.dto.VideoDetailDto;

public interface VideoRepositoryCustom {
    VideoDetailDto findVideoDetailWithLikeCount(Long videoId, String userId);
}

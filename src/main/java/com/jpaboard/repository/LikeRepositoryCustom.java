package com.jpaboard.repository;

import com.jpaboard.entity.Videos;

import java.util.List;

public interface LikeRepositoryCustom {
    int deleteLikeByUserIdAndVideoId(String userId, Long videoId);
    List<Videos> getVideoLikeList();
}

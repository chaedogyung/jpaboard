package com.jpaboard.repository;

public interface LikeRepositoryCustom {
    int deleteLikeByUserIdAndVideoId(String userId, Long videoId);
}

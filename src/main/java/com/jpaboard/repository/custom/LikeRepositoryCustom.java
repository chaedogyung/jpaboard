package com.jpaboard.repository.custom;

public interface LikeRepositoryCustom {
    int deleteLikeByUserIdAndVideoId(String userId, Long videoId);
}

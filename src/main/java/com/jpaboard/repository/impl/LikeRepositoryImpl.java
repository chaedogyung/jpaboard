package com.jpaboard.repository.impl;

import com.jpaboard.entity.QVideoLikes;
import com.jpaboard.repository.LikeRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public int deleteLikeByUserIdAndVideoId(String userId, Long videoId) {
        QVideoLikes videoLikes = QVideoLikes.videoLikes;

        long deletedCount = queryFactory
                .delete(videoLikes)
                .where(
                        videoLikes.userid.userid.eq(userId)
                                .and(videoLikes.video.videoId.eq(videoId))
                )
                .execute();

        return (int) deletedCount; // 삭제된 행 수 반환
    }
}
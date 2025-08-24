package com.jpaboard.repository.impl;

import com.jpaboard.entity.QVideoLikes;
import com.jpaboard.entity.QVideos;
import com.jpaboard.entity.Videos;
import com.jpaboard.repository.LikeRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    //좋아요 표시한 동영상
    @Override
    @Transactional
    public List<Videos> getVideoLikeList() {
        QVideos v = QVideos.videos;

//        BooleanBuilder builder = new BooleanBuilder();

        //쿼리
        return queryFactory
                .select(
                        Projections.fields(
                                Videos.class,
                                v.title,
                                v.video_url,
                                v.thumbnail_url,
                                v.release_date,
                                v.viewCount
                        )
                )
                .from(v)
                .where(v.viewCount.gt(0))
                .orderBy(v.release_date.desc())
                .fetch();
    }

}
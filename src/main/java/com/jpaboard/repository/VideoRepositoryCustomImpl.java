package com.jpaboard.repository;

import com.jpaboard.dto.VideoDetailDto;
import com.jpaboard.entity.QVideoLikes;
import com.jpaboard.entity.QVideos;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VideoRepositoryCustomImpl implements VideoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public VideoDetailDto findVideoDetailWithLikeCount(Long videoId) {
        QVideos video = QVideos.videos;
        QVideoLikes like = QVideoLikes.videoLikes;

        return queryFactory
                .select(Projections.constructor(
                        VideoDetailDto.class,
                        video.video_id,
                        video.title,
                        video.video_url,
                        video.subtitle_file_path,
                        video.release_date,
                        video.age_rating,
                        video.genre,
                        video.language,
                        Expressions.stringTemplate("TO_CHAR({0})", video.description), // SQL 변환
                        like.like_id.count()
                ))
                .from(video)
                .leftJoin(like).on(like.video.video_id.eq(video.video_id))
                .where(video.video_id.eq(videoId))
                .groupBy(
                        video.video_id,
                        video.title,
                        video.video_url,
                        video.subtitle_file_path,
                        video.release_date,
                        video.age_rating,
                        video.genre,
                        video.language,
                        video.description // CLOB 필드도 그룹화에 포함
                )
                .fetchOne();
    }

}

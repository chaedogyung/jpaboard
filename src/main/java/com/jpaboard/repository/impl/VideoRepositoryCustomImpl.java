package com.jpaboard.repository.impl;

import com.jpaboard.dto.SearchDto;
import com.jpaboard.dto.VideoDetailDto;
import com.jpaboard.entity.QVideoLikes;
import com.jpaboard.entity.QVideos;
import com.jpaboard.repository.VideoRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoRepositoryCustomImpl implements VideoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public VideoDetailDto findVideoDetailWithLikeCount(Long videoId, String userId) {
        QVideos video = QVideos.videos;

        return queryFactory
                .select(Projections.fields(
                        VideoDetailDto.class,
                        video.videoId,
                        video.title,
                        video.video_url,
                        video.subtitle_file_path,
                        video.release_date,
                        video.age_rating,
                        video.genre,
                        video.language,
                        video.viewCount,
                        video.description
                ))
                .from(video)
                .where(video.videoId.eq(videoId))
                .fetchOne();
    }

    @Override
    public List<Tuple> integratedSearch1(SearchDto search) {
        QVideos video = QVideos.videos;
        List<Tuple> results = queryFactory
                .select(
                        video.videoId,
                        video.title,
                        Expressions.stringTemplate("TO_CHAR({0})", video.description) // SQL 변환
                )
                .from(video)
                .where(video.title.likeIgnoreCase("%"+search.getFullSearch()+"%")
                        .or(video.description.likeIgnoreCase("%"+search.getFullSearch()+"%"))
                )
                .orderBy(video.videoId.desc()) // videoId 기준 내림차순 정렬
                .fetch();
        return results;
    }
}

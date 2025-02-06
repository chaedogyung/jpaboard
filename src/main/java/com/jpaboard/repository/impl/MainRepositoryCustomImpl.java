package com.jpaboard.repository.impl;

import com.jpaboard.dto.SearchDto;
import com.jpaboard.entity.BoardVO;
import com.jpaboard.entity.QBoardVO;
import com.jpaboard.entity.QVideos;
import com.jpaboard.repository.MainRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MainRepositoryCustomImpl implements MainRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<String, Object> integratedSearch(SearchDto search) {
        Map<String, Object> result = new HashMap<>();
        QBoardVO board = QBoardVO.boardVO;
        QVideos videos = QVideos.videos;

        //게시판 동적검색 조건 추가
        BooleanBuilder builder = new BooleanBuilder();
        if (search.getFullSearch() != null && !search.getFullSearch().isEmpty()) {
            builder.and(
                    board.title.containsIgnoreCase(search.getFullSearch())
                            .or(board.content.containsIgnoreCase(search.getFullSearch()))
            );
        }

        //동영상 동적검색 조건
        BooleanBuilder builder1 = new BooleanBuilder();
        if (search.getFullSearch() != null && !search.getFullSearch().isEmpty()) {
            builder1.and(
                    videos.title.containsIgnoreCase(search.getFullSearch())
                            .or(videos.description.containsIgnoreCase(search.getFullSearch()))
            );
        }

        //게시판 쿼리 실행
        JPAQuery<BoardVO> query = queryFactory
                .selectFrom(board)
                .where(builder)
                .orderBy(board.bno.desc());
        result.put("board", query.fetch());

        return result;
    }
}

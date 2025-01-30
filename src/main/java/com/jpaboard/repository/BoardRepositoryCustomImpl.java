package com.jpaboard.repository;

import com.jpaboard.dto.SearchDto;
import com.jpaboard.entity.BoardVO;
import com.jpaboard.entity.QBoardVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardVO> searchBoards(SearchDto searchDto, Pageable pageable) {
        QBoardVO board = QBoardVO.boardVO;

        //동적 검색 조건 추가
        BooleanBuilder builder = new BooleanBuilder();

        if (searchDto.getTitle() != null && !searchDto.getTitle().isEmpty()) {
            builder.and(board.title.containsIgnoreCase(searchDto.getTitle()));
        }
        if (searchDto.getWriter() != null && !searchDto.getWriter().isEmpty()) {
            builder.and(board.writer.containsIgnoreCase(searchDto.getWriter()));
        }
        if (searchDto.getContent() != null && !searchDto.getContent().isEmpty()) {
            builder.and(board.content.containsIgnoreCase(searchDto.getContent()));
        }
        if (searchDto.getFullSearch() != null && !searchDto.getFullSearch().isEmpty()) {
            builder.and(
                    board.title.containsIgnoreCase(searchDto.getFullSearch())
                            .or(board.writer.containsIgnoreCase(searchDto.getFullSearch()))
                            .or(board.content.containsIgnoreCase(searchDto.getFullSearch()))
            );
        }

        //쿼리 실행
        JPAQuery<BoardVO> query = queryFactory
                .selectFrom(board)
                .where(builder)
                .orderBy(board.bno.desc());
        //페이징 처리
        List<BoardVO> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //전체 데이터 개수 조회
        long total = queryFactory.select(board.count())
                .from(board)
                .where(builder)
                .fetchOne();
        return new PageImpl<>(results, pageable, total);
    }
}
package com.jpaboard.repository;

import com.jpaboard.entity.BoardVO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardVO, Long>, QuerydslPredicateExecutor<BoardVO>, BoardRepositoryCustom {

    List<BoardVO> findByTitle(String title);
    BoardVO findByBno(Long bno);

    List<BoardVO> findByTitleOrContent(String title, String content);

    List<BoardVO> findByBnoLessThanOrderByBnoAsc(int bno);

    @Query("select i from BoardVO i where i.content like %:content% order by i.bno desc")
    List<BoardVO> findByContent(@Param("content") String content);

    @Query(value = "select * from mp_board i where i.content like %:content% order by i.bno desc", nativeQuery = true)
    List<BoardVO> findByContentByNative(String content);
}
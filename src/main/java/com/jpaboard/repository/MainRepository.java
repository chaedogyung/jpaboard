package com.jpaboard.repository;

import com.jpaboard.entity.BoardVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MainRepository extends JpaRepository<BoardVO, Long>, QuerydslPredicateExecutor<BoardVO>, MainRepositoryCustom {
}

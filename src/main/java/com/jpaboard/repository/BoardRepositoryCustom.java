package com.jpaboard.repository;

import com.jpaboard.dto.SearchDto;
import com.jpaboard.entity.BoardVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<BoardVO> searchBoards(SearchDto searchDto, Pageable pageable);
}

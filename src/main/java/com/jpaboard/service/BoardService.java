package com.jpaboard.service;

import com.jpaboard.entity.BoardVO;
import com.jpaboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardVO saveBoard(BoardVO boardVO) {
        return boardRepository.save(boardVO);
    }

    public Page<BoardVO> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
}

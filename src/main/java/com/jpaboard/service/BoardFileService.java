package com.jpaboard.service;

import com.jpaboard.entity.BoardFile;
import com.jpaboard.repository.BoardFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardFileService {


    private final BoardFileRepository boardFileRepository;

    public BoardFile getFile(Long fileNo) {
        return boardFileRepository.findById(fileNo)
                .orElseThrow(() -> new IllegalArgumentException("파일 정보를 찾을 수 없습니다." + fileNo));
    }
}

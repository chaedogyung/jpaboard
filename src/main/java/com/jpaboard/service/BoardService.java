package com.jpaboard.service;

import com.jpaboard.entity.BoardFile;
import com.jpaboard.entity.BoardVO;
import com.jpaboard.repository.BoardFileRepository;
import com.jpaboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    public final String filePath = "E:/boardFile";

    public BoardVO saveBoard(BoardVO boardVO, MultipartFile[] files) {

        BoardVO saveBoard = boardRepository.save(boardVO);

        //첨부파일 저장
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        //서버에 파일 저장
                        String orgFileName = file.getOriginalFilename();
                        String storedFileName = UUID.randomUUID().toString();
                        File targetFile = new File(filePath, orgFileName);
                        file.transferTo(targetFile);

                        //파일정보 저장
                        BoardFile boardFile = new BoardFile();
                        boardFile.setBno(saveBoard.getBno());
                        boardFile.setOrgFileName(orgFileName);
                        boardFile.setStoredFileName(storedFileName);
                        boardFile.setFileSize(file.getSize());
                        boardFileRepository.save(boardFile);
                    } catch (IOException e) {
                        throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
                    }
                }
            }
        }
        return saveBoard;
    }

    public Page<BoardVO> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 상세 조회 메서드
    public BoardVO read(Long bno) {
        return boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. bno: " + bno));
    }

    public List<BoardFile> findFilesByBno(Long bno) {
        return boardFileRepository.findByBno(bno);
    }
}

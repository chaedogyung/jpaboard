package com.jpaboard.service;

import com.jpaboard.dto.SearchDto;
import com.jpaboard.entity.*;
import com.jpaboard.repository.BoardFileRepository;
import com.jpaboard.repository.BoardRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.jpaboard.entity.QBoardFile.boardFile;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    public final String filePath = "E:/boardFile";
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

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

    public Page<BoardVO> boardList(Pageable pageable, SearchDto searchDto) {
        return boardRepository.searchBoards(searchDto, pageable);
    }

    // 상세 조회 메서드
    public BoardVO read(Long bno) {
        return boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. bno: " + bno));
    }

    public List<BoardFile> findFilesByBno(Long bno) {
        return boardFileRepository.findByBno(bno);
    }

    public BoardVO readWithFiles(Long bno) {

        QBoardVO board = QBoardVO.boardVO;
        QBoardFile file = boardFile;

        List<Tuple> results = queryFactory
                .select(board, file)
                .from(board)
                .leftJoin(board.files, file).fetchJoin()
                .where(board.bno.eq(bno))
                .fetch();

        if (results.isEmpty()) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. bno: " + bno);
        }

        return results.get(0).get(board);

    }

    public void boardUpdate(@Valid BoardVO boardVO, MultipartFile[] files, List<String> fileNoList, List<String> delGbList) {
        QBoardVO board = QBoardVO.boardVO;
        QBoardFile boardFile = QBoardFile.boardFile;
        new JPAUpdateClause(em, board)
                .set(board.title, boardVO.getTitle())
                .set(board.content, boardVO.getContent())
                .set(board.updateRegDate, boardVO.getUpdateRegDate())
                .where(board.bno.eq(boardVO.getBno()))
                .execute();


        for (int i = 0; i < fileNoList.size(); i++) {
            new JPAUpdateClause(em, boardFile)
                    .set(boardFile.delGb, delGbList.get(i))
                    .where(boardFile.fileNo.eq(Long.valueOf(fileNoList.get(i))))
                    .execute();
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try{
                    //서버에 파일저장
                    String orgFileName = file.getOriginalFilename();
                    String storedFileName = UUID.randomUUID().toString();
                    File targetFile = new File(filePath, orgFileName);
                    file.transferTo(targetFile);

                    //파일정보 저장
                    BoardFile boardFile1 = new BoardFile();
                    boardFile1.setBno(boardVO.getBno());
                    boardFile1.setOrgFileName(orgFileName);
                    boardFile1.setStoredFileName(storedFileName);
                    boardFile1.setFileSize(file.getSize());
                    boardFileRepository.save(boardFile1);
                } catch (Exception e) {
                    throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
                }
            }
        }
    }
}

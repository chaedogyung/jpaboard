package com.jpaboard.controller;

import com.jpaboard.entity.BoardVO;
import com.jpaboard.entity.MpReply;
import com.jpaboard.service.BoardService;
import com.jpaboard.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;
    private final ReplyService replyService;

    //테스트 상세 뷰
    @GetMapping(value = "/testView")
    public String testView() {
        logger.info("testView");
        return "board/testView";
    }

    //게시판 글쓰기 뷰
    @GetMapping(value = "/writeView")
    public String writeView() {
        logger.info("writeView");
        return "board/writeView";
    }

    //게시판 글쓰기 Post
    @PostMapping(value = "/writeReg")
    public String writeReg(@Valid BoardVO boardVO,
                           BindingResult bindingResult,
                           @RequestParam("file") MultipartFile[] file,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "board/writeView";
        }
        try {
            boardService.saveBoard(boardVO, file);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "board/writeView";
        }
        return "redirect:/board/boardList";
    }

    //게시판 목록 뷰
    @GetMapping(value = "/boardList")
    public String boardList(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        logger.info("boardList");

        // 내림차순 정렬을 위한 PageRequest 생성
        Page<BoardVO> boardList = boardService.boardList(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "bno"))); // "id" 기준 내림차순 정렬

        int totalPages = boardList.getTotalPages();
        int startPage = Math.max(0, page - 5); // 현재 페이지 기준으로 앞 5페이지
        int endPage = Math.min(totalPages - 1, page + 4); // 현재 페이지 기준으로 뒤 4페이지
        if (endPage == 4) {
            endPage = Math.min(totalPages - 1, page + 9); // 현재 페이지 기준으로 뒤 9페이지
        } else {
            endPage = Math.min(totalPages - 1, page + 4); // 현재 페이지 기준으로 뒤 4페이지
        }
        model.addAttribute("list", boardList.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/boardList";
    }

    //게시판 조회
    @GetMapping(value = "/readView")
    public String readView(BoardVO boardVO, Model model) {
        logger.info("read");
        model.addAttribute("read", boardService.read(boardVO.getBno()));
        return "board/readView";
    }

    //댓글 작성
    @PostMapping(value = "/replyWrite")
    public ResponseEntity<String> replyWrite(@RequestParam Long bno,
                                             @RequestParam String reWriter,
                                             @RequestParam String reContent) throws Exception {
        logger.info("replyWrite");
        try {

            replyService.saveReply(bno, reWriter, reContent);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reply created successfully");
        } catch (Exception e) {
            logger.error("Error while writing reply: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create reply.");
        }
    }

    // 댓글 목록 조회
    @GetMapping(value = "/replyList")
    public ResponseEntity<List<MpReply>> replyList(@RequestParam Long bno) {
        try {
            // 게시글 번호를 이용해 BoardVO 객체 생성
            BoardVO board = new BoardVO();
            board.setBno(bno);

            // 댓글 목록 조회
            List<MpReply> replies = replyService.getRepliesByBoard(board);
            // 조회된 댓글 목록 반환
            return ResponseEntity.ok(replies);

        } catch (Exception e) {
            // 예외 발생 시 상세 에러 메시지와 함께 500 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //댓글 수정POST
    @PostMapping(value = "/replyUpdate")
    public ResponseEntity<String> replyUpdate(MpReply vo) throws Exception {
        logger.info("replyUpdate");
        try {
            replyService.updateReply(vo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reply created successfully");
        } catch (Exception e) {
            logger.error("Error while writing reply: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create reply.");
        }
    }

    //댓글 삭제
    @DeleteMapping(value = "/replyDelete")
    public ResponseEntity<String> replyDelete(@RequestParam("reRno") Long reRno) throws Exception {
        logger.info("replyDelete");
        try {
            replyService.deleteReply(reRno);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reply created successfully");
        } catch (Exception e) {
            logger.error("Error while writing reply: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create reply.");
        }
    }
}
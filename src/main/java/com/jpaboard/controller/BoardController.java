package com.jpaboard.controller;

import com.jpaboard.entity.BoardVO;
import com.jpaboard.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;

//    public BoardController(BoardService boardService) {
//        this.boardService = boardService;
//    }

    //테스트 상세 뷰
    @GetMapping(value = "/testView")
    public String testView() {
        logger.info("testView");
        return "board/testView";
    }






    //게시판 상세 뷰
    @GetMapping(value = "/writeView")
    public String writeView() {
        logger.info("writeView");
        return "board/writeView";
    }

    //게시판 글쓰기 Post
    @PostMapping(value = "/writeReg")
    public String writeReg(@Valid BoardVO boardVO,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "board/writeView";
        }
        try {
            boardService.saveBoard(boardVO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "board/writeView";
        }
        return "board/boardList";
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
}

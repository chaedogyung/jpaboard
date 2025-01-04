package com.jpaboard.controller;

import com.jpaboard.entity.BoardVO;
import com.jpaboard.service.BoardService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board/*")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
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
    public String boardList() {
        logger.info("boardList");
        return "board/boardList";
    }
}

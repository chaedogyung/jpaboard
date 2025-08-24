package com.jpaboard.controller;

import com.jpaboard.config.oauth.PrincipalDetails;
import com.jpaboard.dto.MpReplyDto;
import com.jpaboard.dto.SearchDto;
import com.jpaboard.entity.BoardFile;
import com.jpaboard.entity.BoardVO;
import com.jpaboard.entity.Member;
import com.jpaboard.entity.MpReply;
import com.jpaboard.service.BoardFileService;
import com.jpaboard.service.BoardService;
import com.jpaboard.service.ReplyService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final BoardFileService boardFileService;

    //테스트 상세 뷰
    @GetMapping(value = "/testView")
    public String testView() {
        log.info("testView");

        return "Ottfragments/left-sidebar";
    }

    @GetMapping(value = "/layout")
    public String layout() {
        return "OttLayouts/layout1";
    }

    //게시판 글쓰기 뷰
    @GetMapping(value = "/writeView")
    public String writeView(Model model) {
        log.info("writeView");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        model.addAttribute("userId", userId);

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

    //게시판 수정 뷰
    @GetMapping(value = "/boardUpdateView")
    public String boardUpdateView(Model model, @RequestParam("bno") Long bno) {
        log.info("updateView");
        BoardVO boardVO = boardService.readWithFiles(bno);

        int fileSize = boardVO.getFiles().size();
        List<String> delGb = new ArrayList<>();
        for (int i = 0; i < fileSize; i++) {
            delGb.add(boardVO.getFiles().get(i).getDelGb());
        }

        model.addAttribute("updateView", boardVO);
        model.addAttribute("delGb", delGb);

        return "board/updateView";
    }

    //게시판 수정
    @PostMapping(value = "/boardUpdate")
    public String boardUpdate(@Valid BoardVO boardVO,
                              BindingResult bindingResult,
                              @RequestParam("file") MultipartFile[] files,
                              @RequestParam(value = "fileNo", required = false, defaultValue = "") List<String> fileNoList,
                              @RequestParam(value = "delGb", required = false, defaultValue = "") List<String> delGbList,
                              Model model) {
        log.info("boardUpdate");

        if (bindingResult.hasErrors()) {
            return "board/writeView";
        }
        try {
            boardService.boardUpdate(boardVO, files, fileNoList, delGbList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/board/readView?bno=" + boardVO.getBno();
        }
        return "redirect:/board/readView?bno=" + boardVO.getBno();
    }

    //게시판 목록 뷰
    @GetMapping(value = "/boardList")
    public String boardList(@RequestParam(value = "page",
                            defaultValue = "0") int page,
                            SearchDto searchDto,
                            Model model) {

        // 내림차순 정렬을 위한 PageRequest 생성
        log.info("boardList - 검색 조건: {}", searchDto);

        Page<BoardVO> boardList = boardService.boardList(
                PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "bno")),
                searchDto
        );
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
        model.addAttribute("searchDto", searchDto);

        return "board/boardList";
    }

    //게시판 조회
    @GetMapping(value = "/readView")
    public String readView(BoardVO boardVO, Model model) {
        log.info("read");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principal = (PrincipalDetails) auth.getPrincipal();
        Member member = principal.getMember();
        String useremail = member.getUseremail();

        List<BoardFile> attachedFiles = boardService.findFilesByBno(boardVO.getBno());
        model.addAttribute("file", attachedFiles);
        model.addAttribute("read", boardService.read(boardVO.getBno()));
        model.addAttribute("useremail", useremail);

        return "board/readView";
    }

    //댓글 작성
    @PostMapping(value = "/replyWrite")
    public ResponseEntity<String> replyWrite(@RequestParam Long bno,
                                             @RequestParam String reWriter,
                                             @RequestParam String reContent) throws Exception {
        log.info("replyWrite");
        try {
            replyService.saveReply(bno, reWriter, reContent);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reply created successfully");
        } catch (Exception e) {
            log.error("Error while writing reply: ", e);
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
        log.info("replyUpdate");
        try {
            replyService.updateReply(vo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reply created successfully");
        } catch (Exception e) {
            log.error("Error while writing reply: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create reply.");
        }
    }

    //댓글 삭제
    @DeleteMapping(value = "/replyDelete")
    public ResponseEntity<String> replyDelete(@RequestParam("reRno") Long reRno) throws Exception {
        log.info("replyDelete");
        try {
            replyService.deleteReply(reRno);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reply created successfully");
        } catch (Exception e) {
            log.error("Error while writing reply: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create reply.");
        }
    }

    //댓글에 대한 답글 작성
    @PostMapping(value="/replyReplyWrite")
    public ResponseEntity<List<MpReply>> replyReplyWrite(MpReplyDto mpReplyDto){

        log.info("replyReplyWrite");
        try{
            ResponseEntity<List<MpReply>> mpReplyList = replyService.replyReplyWrite(mpReplyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(mpReplyList.getBody());
        } catch(Exception e){
            log.error("Error while writing reply: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    //첨부파일 다운로드
    @GetMapping(value = "/fileDown")
    public void fileDown(BoardFile boardFile, HttpServletResponse response) throws Exception {

        //파일 서비스 호출
        BoardFile boFile = boardFileService.getFile(boardFile.getFileNo());

        //파일경로 가져오기
        String filePath = boardService.filePath + "/" + boFile.getOrgFileName();

        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("파일이 존재하지 않습니다.");
        }

        String encodedFileName = URLEncoder.encode(file.getName(), "UTF-8").replaceAll("\\+", "%20");

        //응답헤더설정
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        response.setHeader("Content-Length", String.valueOf(file.length()));

        //파일을 응답에 작성
        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
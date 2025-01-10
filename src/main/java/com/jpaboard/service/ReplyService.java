package com.jpaboard.service;

import com.jpaboard.entity.BoardVO;
import com.jpaboard.entity.MpReply;
import com.jpaboard.repository.BoardRepository;
import com.jpaboard.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public MpReply saveReply(Long bno, String reWriter, String reContent) {
        MpReply reply = new MpReply();
        reply.setReContent(reContent);
        reply.setReWriter(reWriter);
        BoardVO boardVO = boardRepository.findByBno(bno); //
        reply.setBoardVO(boardVO);
        return replyRepository.save(reply);
    }

    // 특정 게시글에 대한 댓글 목록 조회
    public List<MpReply> getRepliesByBoard(BoardVO boardVO) {
        return replyRepository.findByBoardVO_Bno(boardVO.getBno());
    }

    public void updateReply(MpReply vo) {

        MpReply existingReply = replyRepository.findById(vo.getReRno())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        existingReply.setReContent(vo.getReContent());
        replyRepository.save(existingReply);
    }

    public void deleteReply(Long reRno) {
        replyRepository.deleteById(reRno);
    }
}
